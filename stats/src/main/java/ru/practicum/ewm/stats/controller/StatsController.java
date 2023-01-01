package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StatsController {

    private final StatsService statsService;

    // Сохранение информации о том, что к эндпоинту был запрос
    // Сохранение информации о том,
    // что на uri конкретного сервиса был отправлен запрос пользователем.
    // Название сервиса, uri и ip пользователя указаны в теле запроса.
    @PostMapping("/hit")
    public void updateStats(@RequestBody EndpointHit hit) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос");
        statsService.updateStats(hit);
    }

    // Получение статистики по посещениям
    @GetMapping("/stats")
    public List<ViewStats> getStats(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Получение статистики по посещениям");
        return statsService.getStats(start, end, uris, unique);
    }
}
