package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.UpdateEventRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    // Private: Events - Получение событий, добавленных текущим пользователем
    @GetMapping
    public EventShortDto findUserEvents(
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
            @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Изменение события добавленного пользователем с id={}; {}", userId, updateEventRequest);
        return eventPrivateService.changeEvent(userId, updateEventRequest);
    }

    // Private: Events - Добавление нового события
    @PostMapping
    public EventFullDto addNewEvent(
            @PathVariable Long userId,
            @RequestBody NewEventDto newEventDto) {
        log.info("Добавление нового события {}", newEventDto);
        return eventPrivateService.newEventDto(userId, newEventDto);
    }
}
