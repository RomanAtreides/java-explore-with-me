package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StatsController {

    private final StatsService statsService;

    // Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем
    @PostMapping("/hit")
    public void updateStats(@Valid @RequestBody EndpointHit hit) {
        log.info("Сохранение информации о просмотре события {}", hit);
        statsService.updateStats(hit);
    }

    // Получение статистики по посещениям
    @GetMapping("/stats")
    public List<ViewStats> getStats(
            @NotNull @RequestParam String start,
            @NotNull @RequestParam String end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Получение статистики по посещениям");
        return statsService.getStats(start, end, uris, unique);
    }
}
