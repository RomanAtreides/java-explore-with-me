package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.request.UpdateEventRequest;

public interface EventPrivateService {

    EventShortDto findUserEvents(Long userId, Integer from, Integer size);

    EventFullDto changeEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto newEventDto(Long userId, NewEventDto newEventDto);
}
