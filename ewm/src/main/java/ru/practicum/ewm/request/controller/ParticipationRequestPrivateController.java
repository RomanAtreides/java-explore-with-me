package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.ParticipationRequestPrivateService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestPrivateController {

    private final ParticipationRequestPrivateService participationRequestPrivateService;

    // Private: ParticipationRequests - Получение информации о заявках текущего пользователя
    @GetMapping
    public List<ParticipationRequestDto> findUserParticipationRequests(@NotNull @PathVariable Long userId) {
        log.info("Получение информации о заявках пользователя с id={} на участие в чужих событиях", userId);
        return participationRequestPrivateService.findUserParticipationRequests(userId);
    }

    // Private: ParticipationRequests - Добавление запроса от текущего пользователя на участие в событии
    @PostMapping
    public ParticipationRequestDto addNewParticipationRequest(
            @NotNull @PathVariable Long userId,
            @NotNull @RequestParam Long eventId) {
        log.info("Добавление запроса от пользователя с id={} на участие в событии с id={}", userId, eventId);
        return participationRequestPrivateService.addNewParticipationRequest(userId, eventId);
    }

    // Private: ParticipationRequests - Отмена своего запроса на участие в событии
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long requestId) {
        log.info("Отмена своего запроса с id={} на участие в событии с id={}", requestId, userId);
        return participationRequestPrivateService.cancelParticipationRequest(userId, requestId);
    }
}
