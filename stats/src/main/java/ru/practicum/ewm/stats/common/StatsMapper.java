package ru.practicum.ewm.stats.common;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.dto.EndpointHit;
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
}
