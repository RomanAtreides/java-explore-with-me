package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.common.EventValidator;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.state.EventState;
import ru.practicum.ewm.event.state.ParticipationStatus;
import ru.practicum.ewm.exception.EntityAlreadyExistsException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.common.ParticipationRequestMapper;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.common.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserAdminService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestPrivateServiceImpl implements ParticipationRequestPrivateService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final UserAdminService userAdminService;
    private final EventValidator eventValidator;

    @Override
    public List<ParticipationRequestDto> findUserParticipationRequests(Long userId) {
        List<ParticipationRequest> requests = participationRequestRepository
                .findUserParticipationRequests(userId);

        return requests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addNewParticipationRequest(Long userId, Long eventId) {
        ParticipationRequest existingRequest = participationRequestRepository
                .findParticipationRequestByEventIdAndRequesterId(userId, eventId);

        // Нельзя добавить повторный запрос
        if (existingRequest != null) {
            throw new EntityAlreadyExistsException(String.format(
                    "Заявка на участие в событии с id=%d пользователем с id=%d уже существует",
                    eventId, userId
            ));
        }

        Event event = eventValidator.getEventIfExists(eventId);

        // Инициатор события не может добавить запрос на участие в своём событии
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(String.format(
                    "Пользователь с id=%d не может добавить запрос на участие в своём событии с id=%d",
                    userId, eventId
            ));
        }

        // Нельзя участвовать в неопубликованном событии
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(String.format(
                    "Нельзя оставить запрос на участие в неопубликованном событии с id=%d",
                    eventId
            ));
        }

        // Если у события достигнут лимит запросов на участие, необходимо вернуть ошибку
        Integer participantLimit = event.getParticipantLimit();

        if (event.getConfirmedRequests() + 1 > participantLimit && participantLimit != 0) {
            throw new ValidationException(String.format(
                    "Достигнут лимит запросов на участие в событии с id=%d",
                    eventId
            ));
        }

        /*
         * Если для события отключена пре-модерация запросов на участие,
         * то запрос должен автоматически перейти в состояние подтвержденного
         */
        ParticipationStatus status = event.isRequestModeration() ?
                ParticipationStatus.PENDING :
                ParticipationStatus.CONFIRMED;

        User requester = UserMapper
                .userDtoToUser(userAdminService.findUsers(new Long[]{userId}, 0, 1).get(0));

        final ParticipationRequest request = ParticipationRequest.builder()
                .id(null)
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(status)
                .build();

        final ParticipationRequest entity = participationRequestRepository.save(request);

        return ParticipationRequestMapper.toParticipationRequestDto(entity);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = participationRequestRepository
                .findParticipationRequestByIdAndRequesterId(requestId, userId);

        if (request == null) {
            throw new EntityNotFoundException(String.format("Заявка на участие с id=%d не найдена", requestId));
        }

        if (!request.getStatus().equals(ParticipationStatus.PENDING)) {
            throw new ValidationException(
                    "Отменить можно только заявку находящуюся на рассмотрении"
            );
        }
        request.setStatus(ParticipationStatus.CANCELED);

        ParticipationRequest entity = participationRequestRepository.save(request);

        return ParticipationRequestMapper.toParticipationRequestDto(entity);
    }
}
