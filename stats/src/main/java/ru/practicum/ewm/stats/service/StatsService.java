package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void updateStats(EndpointHit hit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}
