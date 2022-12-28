package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;

@RequiredArgsConstructor
public class EventValidator {

    private final EventRepository eventRepository;

    public Event getEventIfExists(Long eventId) {
        String exceptionMessage = "Event with id=" + eventId + " was not found";

        if (eventId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}