package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.common.CategoryMapper;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.event.common.EventMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.state.EventState;
import ru.practicum.ewm.event.state.ParticipationStatus;
import ru.practicum.ewm.exception.AccessException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.common.ParticipationRequestMapper;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.common.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserAdminService;
import ru.practicum.ewm.user.state.FriendshipStatus;
import ru.practicum.ewm.utility.FromSizeRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EntityManager entityManager;
    private final EventRepository eventRepository;
    private final UserAdminService userAdminService;
    private final CategoryPublicService categoryPublicService;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public List<EventShortDto> findUserEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = FromSizeRequest.of(from, size);
        List<Event> events = eventRepository.findEventsByInitiatorId(userId, pageable);

        return events.stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto changeEvent(Long userId, UpdateEventRequest updateEventRequest) {
        Event eventToUpdate = getEventIfExists(updateEventRequest.getEventId());
        EventState state = eventToUpdate.getState();

        // Изменить можно только отмененные события или события в состоянии ожидания модерации
        if (state.equals(EventState.PUBLISHED)) {
            throw new ValidationException("Только события находящиеся на модерации могут быть изменены");
        }

        // Если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
        if (state.equals(EventState.CANCELED)) {
            eventToUpdate.setState(EventState.PENDING);
        }

        // Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        LocalDateTime newEventDate = updateEventRequest.getEventDate();

        checkEventDate(newEventDate);

        String newAnnotation = updateEventRequest.getAnnotation();
        Long categoryId = updateEventRequest.getCategory();
        String newDescription = updateEventRequest.getDescription();
        boolean paid = updateEventRequest.isPaid();
        Integer newParticipantLimit = updateEventRequest.getParticipantLimit();
        String newTitle = updateEventRequest.getTitle();
        CategoryDto categoryDto = categoryPublicService.findCategoryById(categoryId);

        if (newAnnotation != null) {
            eventToUpdate.setAnnotation(newAnnotation);
        }

        if (categoryDto != null) {
            eventToUpdate.setCategory(CategoryMapper.categoryDtoToCategory(categoryDto));
        }

        if (newDescription != null) {
            eventToUpdate.setDescription(newDescription);
        }

        eventToUpdate.setEventDate(newEventDate);
        eventToUpdate.setPaid(paid);

        if (newParticipantLimit != null) {
            eventToUpdate.setParticipantLimit(newParticipantLimit);
        }

        if (newTitle != null) {
            eventToUpdate.setTitle(newTitle);
        }
        eventRepository.save(eventToUpdate);
        return EventMapper.eventToEventFullDto(eventToUpdate);
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {
        checkEventDate(newEventDto.getEventDate());

        UserDto userDto = userAdminService.findUsers(new Long[]{userId}, 0, 1).get(0);
        final User initiator = UserMapper.userDtoToUser(userDto);

        final Category category = CategoryMapper.categoryDtoToCategory(
                categoryPublicService.findCategoryById(newEventDto.getCategory())
        );

        final Event event = EventMapper.newEventDtoToEvent(
                newEventDto, initiator, category, 0L, 0L
        );

        final Event entity = eventRepository.save(event);

        return EventMapper.eventToEventFullDto(entity);
    }

    @Override
    public EventFullDto findUserEventFullInfo(Long userId, Long eventId) {
        Event event = getEventIfExists(eventId);

        checkIfUserIsInitiator(userId, eventId, event);
        return EventMapper.eventToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        Event event = getEventIfExists(eventId);

        checkIfUserIsInitiator(userId, eventId, event);

        if (!event.getState().equals(EventState.PENDING)) {
            throw new AccessException("Только события находящиеся на модерации могут быть изменены");
        }
        event.setState(EventState.CANCELED);
        Event canceledEvent = eventRepository.save(event);
        return EventMapper.eventToEventFullDto(canceledEvent);
    }

    @Override
    public List<ParticipationRequestDto> findUserEventParticipationRequests(Long userId, Long eventId) {
        List<ParticipationRequest> requests = participationRequestRepository.findParticipationRequestsByEventId(eventId);

        return requests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<Long, ParticipationRequestDto> confirmParticipationRequest(Long userId, Long eventId, Long reqId) {
        ParticipationRequest request = getParticipationRequestIfExists(reqId);

        if (request.getStatus().equals(ParticipationStatus.CONFIRMED)) {
            throw new ValidationException(String.format("Заявка с id=%d уже подтверждена", reqId));
        }

        Event event = request.getEvent();
        Integer limit = event.getParticipantLimit();
        Long confirmedRequests = event.getConfirmedRequests();
        Long canceledRequestsQuantity = 0L;

        /*
         * Если для события лимит заявок равен 0 или отключена пре-модерация заявок,
         * то подтверждение заявок не требуется
         */
        if (limit == 0 || !event.isRequestModeration()) {
            request.setStatus(ParticipationStatus.DOES_NOT_REQUIRE_CONFIRMATION);
            return Map.of(canceledRequestsQuantity, ParticipationRequestMapper.toParticipationRequestDto(request));
        }

        // Нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
        if (confirmedRequests + 1 > limit) {
            request.setStatus(ParticipationStatus.REJECTED);
            return Map.of(canceledRequestsQuantity, ParticipationRequestMapper.toParticipationRequestDto(request));
        }
        request.setStatus(ParticipationStatus.CONFIRMED);
        event.setConfirmedRequests(confirmedRequests + 1);

        /*
         * Если при подтверждении данной заявки, лимит заявок для события исчерпан,
         * то все неподтверждённые заявки необходимо отклонить
         */
        if (confirmedRequests + 1 == limit) {
            canceledRequestsQuantity = participationRequestRepository.findParticipationRequestsByEventId(eventId).stream()
                    .filter(r -> r.getStatus().equals(ParticipationStatus.PENDING))
                    .peek(r -> r.setStatus(ParticipationStatus.REJECTED))
                    .count();
        }

        ParticipationRequest consideredRequest = participationRequestRepository.save(request);

        return Map.of(canceledRequestsQuantity, ParticipationRequestMapper.toParticipationRequestDto(consideredRequest));
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long reqId) {
        ParticipationRequest request = getParticipationRequestIfExists(reqId);

        if (request.getStatus().equals(ParticipationStatus.REJECTED)) {
            throw new ValidationException(String.format("Заявка с id=%d уже отменена", reqId));
        }
        request.setStatus(ParticipationStatus.REJECTED);

        ParticipationRequest canceledRequest = participationRequestRepository.save(request);

        return ParticipationRequestMapper.toParticipationRequestDto(canceledRequest);
    }

    // TODO: 15.01.2023 реализовать метод
    /*
     * Подписка на друзей и возможность получать список актуальных событий, в которых они принимают участие
     * Список событий, которые организовал пользователь
     */
    @Override
    public List<EventShortDto> findFriendsEvents(Long userId, Boolean descendingSort, Integer from, Integer size) {
        String sort = descendingSort ? "DESC" : "ASC";

        Query queryToGetFriendsEvents = entityManager.createNativeQuery(
                "SELECT * " +
                        "FROM events " +
                        "WHERE id IN " +
                        "(SELECT DISTINCT event_id " +
                        "FROM participation_requests " +
                        "WHERE requester_id IN " +
                        "(SELECT requester_id " +
                        "FROM friendship " +
                        "WHERE friend_id = ?1 " +
                        "AND friendship_status = ?2 " +
                        "UNION " +
                        "SELECT friend_id " +
                        "FROM friendship " +
                        "WHERE requester_id = ?1 " +
                        "AND friendship_status = ?2) " +
                        "AND (status = ?3 OR status = ?4)) " +
                        "AND state = ?5 ORDER BY event_date " + sort + " OFFSET ?6 LIMIT ?7", Event.class
        );

        List<?> resultList = queryToGetFriendsEvents
                .setParameter(1, userId)
                .setParameter(2, String.valueOf(FriendshipStatus.CONFIRMED))
                .setParameter(3, String.valueOf(ParticipationStatus.CONFIRMED))
                .setParameter(4, String.valueOf(ParticipationStatus.PENDING))
                .setParameter(5, String.valueOf(EventState.PUBLISHED))
                .setParameter(6, from)
                .setParameter(7, size)
                .getResultList();

        return resultList.stream()
                .map(r -> EventMapper.eventToEventShortDto((Event) r))
                .collect(Collectors.toList());
    }

    private Event getEventIfExists(Long eventId) {
        String exceptionMessage = String.format("Событие с id=%d не найдено", eventId);

        if (eventId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }

    private ParticipationRequest getParticipationRequestIfExists(Long reqId) {
        return participationRequestRepository.findById(reqId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Заявка на участие с id=%d не найдена", reqId)));
    }

    private static void checkEventDate(LocalDateTime newEventDate) {
        if (newEventDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата и время на которые намечено событие " +
                    "не может быть раньше, чем через два часа от текущего момента");
        }
    }

    private static void checkIfUserIsInitiator(Long userId, Long eventId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("Событие с id=%d не найдено", eventId));
        }
    }
}
