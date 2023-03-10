package ru.practicum.ewm.stats.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.common.StatsMapper;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.model.QStats;
import ru.practicum.ewm.stats.model.Stats;
import ru.practicum.ewm.stats.repository.StatsRepository;

import javax.persistence.EntityManager;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void updateStats(EndpointHit hit) {
        Stats stats = StatsMapper.toStat(hit);

        statsRepository.save(stats);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, String[] uris, Boolean unique) {
        LocalDateTime[] intervalBounds = getDatesForInterval(start, end);

        QStats qStats = QStats.stats;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<Stats> query = queryFactory.select(qStats)
                .from(qStats)
                .where(qStats.timestamp.between(intervalBounds[0], intervalBounds[1]));

        if (uris != null) {
            query.where(qStats.uri.in(uris));
        }

        if (unique) {
            query.distinct();
        }

        List<Tuple> tuples = query.select(qStats.app, qStats.uri, qStats.uri.count())
                .from(qStats)
                .groupBy(qStats.app, qStats.uri)
                .fetch();

        return getViewStatsFromTuples(tuples);
    }

    private List<ViewStats> getViewStatsFromTuples(List<Tuple> tuples) {
        return tuples.stream()
                .map(tuple -> new ViewStats(
                        tuple.get(0, String.class),
                        tuple.get(1, String.class),
                        tuple.get(2, Long.class)
                ))
                .collect(Collectors.toList());
    }

    private LocalDateTime[] getDatesForInterval(String encodedStart, String encodedEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String decodedStart = URLDecoder.decode(encodedStart, StandardCharsets.UTF_8);
        String decodedEnd = URLDecoder.decode(encodedEnd, StandardCharsets.UTF_8);
        LocalDateTime start = LocalDateTime.parse(decodedStart, formatter);
        LocalDateTime end = LocalDateTime.parse(decodedEnd, formatter);

        return new LocalDateTime[]{start, end};
    }
}
