package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.utility.marker.Create;
import ru.practicum.ewm.utility.marker.Update;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;
    private final String eventIdUrlPart = "/{eventId}";

    // Private: Events - Получение событий, добавленных текущим пользователем
    @GetMapping
    public List<EventShortDto> findUserEvents(
            @NotNull @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение событий from={}, size={}, добавленных пользователем с id={}", from, size, userId);
        return eventPrivateService.findUserEvents(userId, from, size);
    }

    // Private: Events - Изменение события добавленного текущим пользователем
    @PatchMapping
    public EventFullDto changeEvent(
            @NotNull @PathVariable Long userId,
            @Validated(Update.class) @RequestBody UpdateEventRequest updateEventRequest) {
        log.info(
                "Изменение события с id={} добавленного пользователем с id={}",
                updateEventRequest.getEventId(),
                userId
        );
        return eventPrivateService.changeEvent(userId, updateEventRequest);
    }

    // Private: Events - Добавление нового события
    @PostMapping
    public EventFullDto addNewEvent(
            @NotNull @PathVariable Long userId,
            @Validated(Create.class) @RequestBody NewEventDto newEventDto) {
        EventFullDto eventFullDto = eventPrivateService.addNewEvent(userId, newEventDto);

        log.info("Добавление нового события {}", newEventDto);
        return eventFullDto;
    }

    // Private: Events - Получение полной информации о событии добавленном текущим пользователем
    @GetMapping(eventIdUrlPart)
    public EventFullDto findUserEventFullInfo(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long eventId) {
        log.info("Получение полной информации о событии с id={} добавленном пользователем с id={}", eventId, userId);
        return eventPrivateService.findUserEventFullInfo(userId, eventId);
    }

    // Private: Events - Отмена события добавленного текущим пользователем
    @PatchMapping(eventIdUrlPart)
    public EventFullDto cancelUserEvent(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long eventId) {
        log.info("Отмена события с id={} добавленного пользователем с id={}", eventId, userId);
        return eventPrivateService.cancelUserEvent(userId, eventId);
    }

    // Private: Events - Получение информации о запросах на участие в событии текущего пользователя
    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findUserParticipationRequests(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long eventId) {
        log.info(
                "Получение информации о запросах на участие в событии с id={} пользователем с id={}",
                eventId,
                userId
        );
        return eventPrivateService.findUserEventParticipationRequests(userId, eventId);
    }

    // Private: Events - Подтверждение чужой заявки на участие в событии текущего пользователя
    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipationRequest(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long eventId,
            @NotNull @PathVariable Long reqId) {
        log.info(
                "Подтверждение чужой заявки с id={} на участие в событии с id={} пользователя с id={}",
                reqId,
                eventId,
                userId
        );
        Map<Long, ParticipationRequestDto> map = eventPrivateService.confirmParticipationRequest(userId, eventId, reqId);
        Long key = map.keySet().stream().findFirst().orElse(0L);

        log.info(String.format("Количество отменённых заявок=%d", key));
        return map.get(key);
    }

    // Private: Events - Отклонение чужой заявки на участие в событии текущего пользователя
    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipationRequest(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long eventId,
            @NotNull @PathVariable Long reqId) {
        log.info(
                "Отклонение чужой заявки с id={} на участие в событии с id={} пользователя с id={}",
                userId,
                eventId,
                reqId
        );
        return eventPrivateService.rejectParticipationRequest(userId, eventId, reqId);
    }


    // Private: Events - Просмотр списка событий, в которых участвуют друзья
    @GetMapping("/friendship")
    List<EventShortDto> findFriendsEvents(
            @NotNull @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "false") Boolean descendingSort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Просмотр списка событий, в которых участвуют друзья");
        return eventPrivateService.findFriendsEvents(userId, descendingSort, from, size);
    }
}
