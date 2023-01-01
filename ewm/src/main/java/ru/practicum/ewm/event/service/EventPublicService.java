package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

public interface EventPublicService {

    List<EventShortDto> findFilteredEvents(
            String text,
            Long[] categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size
    );

    EventFullDto findFullEventInfo(Long eventId, String clientIp, String endpointPath);
}
