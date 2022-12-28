package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.request.AdminUpdateEventRequest;

import java.util.List;

public interface EventAdminService {
    List<EventFullDto> findEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size
    );

    EventFullDto changeEvent(Long eventId, AdminUpdateEventRequest request);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);
}
