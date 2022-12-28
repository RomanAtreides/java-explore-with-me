package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.AccessException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.UpdateEventRequest;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserAdminService;
import ru.practicum.ewm.utility.FromSizeRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventRepository eventRepository;
    private final UserAdminService userAdminService;
    private final CategoryPublicService categoryPublicService;

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
        // изменить можно только отмененные события или события в состоянии ожидания модерации
        Event eventToUpdate = getEventIfExists(updateEventRequest.getEventId());
        EventState state = eventToUpdate.getState();

        if (state.equals(EventState.PUBLISHED)) {
            throw new AccessException("Только события находящиеся на модерации могут быть изменены");
        }

        // если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
        if (state.equals(EventState.CANCELED)) {
            eventToUpdate.setState(EventState.PENDING);
        }

        // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
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
        final User initiator = UserMapper.toUser(userDto);

        final Category category = CategoryMapper.categoryDtoToCategory(
                categoryPublicService.findCategoryById(newEventDto.getCategory())
        );

        final Long participationRequestsQuantity = 0L; // Количество одобренных заявок на участие в данном событии
        // if ParticipationRequestDto.getStatus.equals(ParticipationStatus.CONFIRMED);

        final Long views = 0L; // Взять из сервиса статистики

        final Event event = EventMapper.newEventDtoToEvent(
                newEventDto, initiator, category, participationRequestsQuantity, views
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
        return null;
    }

    private Event getEventIfExists(Long eventId) {
        String exceptionMessage = String.format("Событие с id=%d не найдено", eventId);

        if (eventId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }

    private static void checkEventDate(LocalDateTime newEventDate) {
        if (newEventDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException(
                    "Дата и время на которые намечено событие не может быть раньше," +
                            "чем через два часа от текущего момента"
            );
        }
    }

    private static void checkIfUserIsInitiator(Long userId, Long eventId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("Событие с id=%d не найдено", eventId));
        }
    }
}
