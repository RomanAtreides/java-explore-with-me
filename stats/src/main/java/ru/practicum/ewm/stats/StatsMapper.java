package ru.practicum.ewm.stats;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.model.Stats;

@Component
public class StatsMapper {

    public static Stats toStat(EndpointHit hit) {
        return new Stats(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public static ViewStats toViewStats(Stats stats) {
        return new ViewStats(
                stats.getApp(),
                stats.getUri(),
                stats.getId()
        );
    }
}
