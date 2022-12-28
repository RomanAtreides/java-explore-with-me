package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    // @Query("select r from ParticipationRequest r where r.event.id = ?1 and r.requester.id = ?2")
    ParticipationRequest findParticipationRequestByEventIdAndRequesterId(Long eventId, Long userId);
}
