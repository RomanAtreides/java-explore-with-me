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
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.UpdateEventRequest;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserAdminService;
import ru.practicum.ewm.utility.FromSizeRequest;

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
        String annotation = updateEventRequest.getAnnotation();
        Long categoryId = updateEventRequest.getCategory();
        String description = updateEventRequest.getDescription();
        String eventDate = updateEventRequest.getEventDate();
        boolean paid = updateEventRequest.isPaid();
        Integer participantLimit = updateEventRequest.getParticipantLimit();
        String title = updateEventRequest.getTitle();
        Event eventToUpdate = getEventIfExists(updateEventRequest.getEventId());
        CategoryDto categoryDto = categoryPublicService.findCategoryById(categoryId);

        if (annotation != null) {
            eventToUpdate.setAnnotation(annotation);
        }

        if (categoryDto != null) {
            eventToUpdate.setCategory(CategoryMapper.categoryDtoToCategory(categoryDto));
        }

        if (description != null) {
            eventToUpdate.setDescription(description);
        }

        if (eventDate != null) {
            eventToUpdate.setEventDate(eventDate);
        }

        eventToUpdate.setPaid(paid);

        if (participantLimit != null) {
            eventToUpdate.setParticipantLimit(participantLimit);
        }

        if (title != null) {
            eventToUpdate.setTitle(title);
        }
        eventRepository.save(eventToUpdate);
        return EventMapper.eventToEventFullDto(eventToUpdate);
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {
        UserDto userDto = userAdminService.findUsers(new Long[]{userId}, 0, 1).get(0);
        final User initiator = UserMapper.toUser(userDto);

        final Category category = CategoryMapper.categoryDtoToCategory(
                categoryPublicService.findCategoryById(newEventDto.getCategory())
        );

        final Long participationRequestsQuantity = 0L; // Количество одобренных заявок на участие в данном событии
        // if ParticipationRequestDto.getStatus.equals(ParticipationStatus.CONFIRMED);

        final Long views = 0L; // Взять из сервиса статистики

        final Event event = EventMapper.newEventDtoToEvent(newEventDto, initiator, category, participationRequestsQuantity, views);
        final Event entity = eventRepository.save(event);

        return EventMapper.eventToEventFullDto(entity);
    }

    private Event getEventIfExists(Long eventId) {
        String exceptionMessage = "Event with id=" + eventId + " was not found";

        if (eventId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}
