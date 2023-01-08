package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findParticipationRequestByEventIdAndRequesterId(Long eventId, Long userId);

    List<ParticipationRequest> findParticipationRequestsByEventId(Long eventId);

    @Query("select r from ParticipationRequest r where r.requester.id = ?1 and r.event.initiator.id != ?1")
    List<ParticipationRequest> findUserParticipationRequests(Long userId);

    ParticipationRequest findParticipationRequestByIdAndRequesterId(Long requestId, Long userId);
}
