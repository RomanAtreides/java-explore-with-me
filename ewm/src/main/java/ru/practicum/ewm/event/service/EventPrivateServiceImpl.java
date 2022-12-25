package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.UpdateEventRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventRepository eventRepository;

    @Override
    public EventShortDto findUserEvents(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto changeEvent(Long userId, UpdateEventRequest updateEventRequest) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto newEventDto(Long userId, NewEventDto newEventDto) {
        return null;
    }
}
