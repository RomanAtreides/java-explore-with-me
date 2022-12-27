package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.UpdateEventRequest;
import ru.practicum.ewm.utility.marker.Create;
import ru.practicum.ewm.utility.marker.Update;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    // Private: Events - Получение событий, добавленных текущим пользователем
    @GetMapping
    public List<EventShortDto> findUserEvents(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение событий from={}, size={}, добавленных пользователем с id={}", from, size, userId);
        return eventPrivateService.findUserEvents(userId, from, size);
    }

    // Private: Events - Изменение события добавленного текущим пользователем
    @PatchMapping
    public EventFullDto changeEvent(
            @PathVariable Long userId,
            @Validated(Update.class) @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Изменение события с id={} добавленного пользователем с id={}", updateEventRequest.getEventId(), userId);
        return eventPrivateService.changeEvent(userId, updateEventRequest);
    }

    // Private: Events - Добавление нового события
    @PostMapping
    public EventFullDto addNewEvent(
            @PathVariable Long userId,
            @Validated(Create.class) @RequestBody NewEventDto newEventDto) {
        EventFullDto eventFullDto = eventPrivateService.addNewEvent(userId, newEventDto);

        log.info("Добавление нового события {}", newEventDto);
        return eventFullDto;
    }
}