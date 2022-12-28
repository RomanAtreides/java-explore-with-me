package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.event.EventValidator;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.AdminUpdateEventRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private final EventValidator eventValidator;
    private final EventRepository eventRepository;

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
        // Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
        return null;
    }

    @Override
    public EventFullDto changeEvent(Long eventId, AdminUpdateEventRequest request) {
        // Редактирование данных любого события администратором. Валидация данных не требуется
        return null;
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventValidator.getEventIfExists(eventId);
        LocalDateTime publishDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        // дата начала события должна быть не ранее чем за час от даты публикации
        if (publishDateTime.plusHours(1L).isAfter(event.getEventDate())) {
            throw new ValidationException(String.format(
                    "Дата начала события с id=%d должна быть не ранее чем за час от даты публикации", eventId
            ));
        }

        // событие должно быть в состоянии ожидания публикации
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException(String.format(
                    "Событие с id=%d должно быть в состоянии ожидания публикации", eventId
            ));
        }
        event.setPublishedOn(publishDateTime);
        event.setState(EventState.PUBLISHED);
        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        // Обратите внимание: событие не должно быть опубликовано
        return null;
    }
}
