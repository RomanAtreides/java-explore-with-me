package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.StatsMapper;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.model.Stats;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void updateStats(EndpointHit hit) {
        Stats stats = StatsMapper.toStat(hit);

        statsRepository.save(stats);
    }

    // Получение статистики по посещениям.
    // Обратите внимание: значение даты и времени нужно закодировать
    // (например используя java.net.URLEncoder.encode)
    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        List<Stats> stats = statsRepository.findAll();

        return stats.stream()
                .map(StatsMapper::toViewStats)
                .collect(Collectors.toList());
    }
}
