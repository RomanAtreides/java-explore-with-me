package ru.practicum.ewm.event.service;

import com.querydsl.jpa.impl.JPAQuery;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.QEvent;

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
            Integer size,
            String clientIp,
            String endpointPath
    );

    EventFullDto findFullEventInfo(Long eventId, String clientIp, String endpointPath);

    JPAQuery<Event> setDatesForQuery(String rangeStart, String rangeEnd, JPAQuery<Event> query, QEvent qEvent);
}
