package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Component
@RequiredArgsConstructor
public class EventValidator {

    private final EventRepository eventRepository;

    public Event getEventIfExists(Long eventId) {
        String exceptionMessage = String.format("Событие с id=%d не найдено", eventId);

        if (eventId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }

    public Event getEventIfExistsByNativeQuery(Long eventId, Query query) {
        Event event;

        try {
            event = (Event) query.setParameter(1, eventId).getSingleResult();
        } catch (NoResultException exception) {
            throw new EntityNotFoundException(String.format("Событие с id=%d не найдено", eventId));
        }
        return event;
    }
}
