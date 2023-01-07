package ru.practicum.ewm.event.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventValidator;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.state.EventState;
import ru.practicum.ewm.exception.ValidationException;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private final EntityManager entityManager;
    private final EventValidator eventValidator;
    private final EventRepository eventRepository;
    private final CategoryPublicService categoryPublicService;
    private final EventPublicService eventPublicService;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size) {
        QEvent qEvent = QEvent.event;
        //QParticipationRequest qRequest = QParticipationRequest.participationRequest;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<Event> query = queryFactory.select(qEvent)
                .from(qEvent);

        if (users != null) {
            query = query.where(qEvent.initiator.id.in(users));
        }

        if (states != null) {
            query = query.where(qEvent.state.in(Arrays.stream(states)
                    .map(EventState::from)
                    .collect(Collectors.toList())));
        }

        if (categories != null) {
            query = query.where(qEvent.category.id.in(categories));
        }

        // Если диапазон дат не указан, то будут возвращены события, которые произойдут позже текущей даты и времени
        query = eventPublicService.setDatesForQuery(rangeStart, rangeEnd, query, qEvent);
        query = query.limit(size).offset(from);
        List<Event> events = query.fetch();

        return events.stream()
                .map(EventMapper::eventToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto changeEvent(Long eventId, AdminUpdateEventRequest request) {
        Event event = eventValidator.getEventIfExists(eventId);

        String newAnnotation = request.getAnnotation();
        Long newCategory = request.getCategory();
        String newDescription = request.getDescription();
        LocalDateTime newDateTime = request.getEventDate();
        Location newLocation = request.getLocation();
        Boolean newPaid = request.getPaid();
        Integer newParticipantLimit = request.getParticipantLimit();
        Boolean newRequestModeration = request.getRequestModeration();
        String newTitle = request.getTitle();

        if (newAnnotation != null) {
            event.setAnnotation(newAnnotation);
        }

        if (newCategory != null) {
            event.setCategory(CategoryMapper.categoryDtoToCategory(
                    categoryPublicService.findCategoryById(newCategory)
            ));
        }

        if (newDescription != null) {
            event.setDescription(request.getDescription());
        }

        if (newDateTime != null) {
            event.setEventDate(request.getEventDate());
        }

        if (newLocation != null) {
            event.setLocation(request.getLocation());
        }

        if (newPaid != null) {
            event.setPaid(request.getPaid());
        }

        if (newParticipantLimit != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (newRequestModeration != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (newTitle != null) {
            event.setTitle(request.getTitle());
        }

        Event entity = eventRepository.save(event);

        return EventMapper.eventToEventFullDto(entity);
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventValidator.getEventIfExists(eventId);
        LocalDateTime publishDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        // Дата начала события должна быть не ранее чем за час от даты публикации
        if (publishDateTime.plusHours(1L).isAfter(event.getEventDate())) {
            throw new ValidationException(String.format(
                    "Дата начала события с id=%d должна быть не ранее чем за час от даты публикации", eventId
            ));
        }
        checkIfEventIsPending(event, eventId);
        event.setPublishedOn(publishDateTime);
        event.setState(EventState.PUBLISHED);
        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventValidator.getEventIfExists(eventId);

        checkIfEventIsPending(event, eventId);
        event.setState(EventState.CANCELED);
        Event entity = eventRepository.save(event);
        return EventMapper.eventToEventFullDto(entity);
    }

    private void checkIfEventIsPending(Event event, Long eventId) {
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException(String.format(
                    "Событие с id=%d должно быть в состоянии ожидания публикации", eventId
            ));
        }
    }
}
