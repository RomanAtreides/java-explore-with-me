package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
