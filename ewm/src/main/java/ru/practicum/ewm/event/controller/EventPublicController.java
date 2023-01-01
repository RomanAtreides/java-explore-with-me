package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventPublicController {

    private final EventPublicService eventPublicService;

    // Из ТЗ:
    // Каждый публичный запрос для получения списка событий
    // или полной информации о мероприятии
    // должен фиксироваться сервисом статистики

    // Ручки для сохранения статистики:
    // * GET /events, который отвечает за получение событий с возможностью фильтрации.
    // * GET /events/{id}, который позволяет получить подробную информацию об опубликованном событии по его идентификатору.

    // HttpServletRequest request
    // request.getRemoteAddr(); // client ip
    // request.getRequestURI(); // endpoint path

    // Public: Events - Получение событий с возможностью фильтрации
    @GetMapping
    public List<EventShortDto> findFilteredEvents(
            @RequestParam String text,              // текст для поиска в содержимом аннотации и подробном описании события
            @RequestParam Long[] categories,        // список идентификаторов категорий в которых будет вестись поиск
            @RequestParam Boolean paid,             // поиск только платных/бесплатных событий
            @RequestParam String rangeStart,        // дата и время не раньше которых должно произойти событие
            @RequestParam String rangeEnd,          // дата и время не позже которых должно произойти событие
            @RequestParam Boolean onlyAvailable,    // только события у которых не исчерпан лимит запросов на участие
            @RequestParam String sort,                                              // Вариант сортировки: по дате события или по количеству просмотров; EVENT_DATE, VIEWS
            @RequestParam(required = false, defaultValue = "0") Integer from,       // количество событий, которые нужно пропустить для формирования текущего набора
            @RequestParam(required = false, defaultValue = "10") Integer size) {    // количество событий в наборе
        log.info("Получение событий с возможностью фильтрации");
        return eventPublicService.findFilteredEvents(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size
        );
    }

    // Public: Events - Получение подробной информации об опубликованном событии по его идентификатору
    @GetMapping("/{id}")
    public EventFullDto findFullEventInfo(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии с id={}", id);
        return eventPublicService.findFullEventInfo(id, request.getRemoteAddr(), request.getRequestURI());
    }
}
