package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;

import java.util.List;
import java.util.Map;

public interface EventPrivateService {

    List<EventShortDto> findUserEvents(Long userId, Integer from, Integer size);

    EventFullDto changeEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto addNewEvent(Long userId, NewEventDto newEventDto);

    EventFullDto findUserEventFullInfo(Long userId, Long eventId);

    EventFullDto cancelUserEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> findUserEventParticipationRequests(Long userId, Long eventId);

    Map<Long, ParticipationRequestDto> confirmParticipationRequest(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long reqId);

    List<EventShortDto> findFriendsEvents(Long userId, Boolean descendingSort, Integer from, Integer size);
}
