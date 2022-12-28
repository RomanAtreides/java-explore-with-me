package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.ParticipationStatus;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.ParticipationRequestMapper;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestPrivateServiceImpl implements ParticipationRequestPrivateService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final EventPrivateService eventPrivateService;

    @Override
    public List<ParticipationRequestDto> findUserParticipationRequests(Long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto addNewParticipationRequest(Long userId, Long eventId) {
        Event event = EventMapper.eventFullDtoToEvent(eventPrivateService.findUserEventFullInfo(userId, eventId));

        User requester = null;

        final ParticipationRequest request = ParticipationRequest.builder()
                .id(null)
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(ParticipationStatus.PENDING)
                .build();
        return ParticipationRequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        return null;
    }
}
