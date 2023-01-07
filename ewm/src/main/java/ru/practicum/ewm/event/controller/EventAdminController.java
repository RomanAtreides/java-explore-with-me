package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.EventAdminService;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventAdminService eventAdminService;

    // Admin: Event - Поиск событий подходящих под переданные условия
    @GetMapping
    public List<EventFullDto> findEvents(
            @RequestParam(required = false) Long[] users,
            @RequestParam(required = false) String[] states,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Поиск событий по параметрам: " +
                        "users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventAdminService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    // Admin: Event - Редактирование события
    @PutMapping("/{eventId}")
    public EventFullDto changeEvent(
            @PathVariable Long eventId,
            @RequestBody AdminUpdateEventRequest request) {
        log.info("Редактирование события с id={}", eventId);
        return eventAdminService.changeEvent(eventId, request);
    }

    // Admin: Event - Публикация события
    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Публикация события с id={}", eventId);
        return eventAdminService.publishEvent(eventId);
    }

    // Admin: Event - Отклонение события
    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        log.info("Отклонение события с id={}", eventId);
        return eventAdminService.rejectEvent(eventId);
    }
}
