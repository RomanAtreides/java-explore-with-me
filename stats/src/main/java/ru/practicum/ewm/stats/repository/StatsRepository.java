package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.stats.model.Stats;

public interface StatsRepository extends JpaRepository<Stats, Long>, QuerydslPredicateExecutor<Stats> {
}
