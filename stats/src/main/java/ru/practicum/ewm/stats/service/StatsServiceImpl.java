package ru.practicum.ewm.stats.service;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.StatsMapper;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.model.QStats;
import ru.practicum.ewm.stats.model.Stats;
import ru.practicum.ewm.stats.repository.StatsRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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

    // Получение статистики по посещениям.
    // Обратите внимание: значение даты и времени нужно закодировать
    // (например используя java.net.URLEncoder.encode)
    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        QStats qStats = QStats.stats;

        JPAQuery<Stats> query = new JPAQuery<Stats>(entityManager)
                .select(qStats)
                .from(qStats)
                .where(qStats.timestamp.between(start, end));

        if (uris != null) {
            query = query.where(qStats.uri.in(uris));
        }

        if (unique) {
            query = query.distinct();
        }

        List<Stats> st = query.fetch();

        return st.stream()
                .map(stats -> new ViewStats(stats.getApp(), stats.getUri(), 1L))
                .collect(Collectors.toList());
    }

    /*@Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        List<Object[]> objectsFromQuery = entityManager.createQuery(
                "select s.app, s.uri, count(s.uri) from Stats s where s.timestamp >= ?1 and s.timestamp <= ?2 group by s.app, s.uri",
                Object[].class
        ).setParameter(1, start).setParameter(2, end).getResultList();

        List<ViewStats> viewStats = new ArrayList<>();

        for (Object[] object : objectsFromQuery) {
            ViewStats view = new ViewStats();

            view.setApp((String) object[0]);
            view.setUri((String) object[1]);
            view.setHits((Long) object[2]);
            viewStats.add(view);
        }
        return viewStats;
    }*/
}
