package ru.practicum.ewm.friendship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.friendship.service.FriendshipService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    // Friendship - Создание заявки на добавление пользователя в свой список друзей
    @PostMapping("/{userId}/add/{friendId}")
    public void addNewFriendshipRequest(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long friendId) {
        log.info("Добавление пользователем с id={} пользователя с id={} в свой список друзей", userId, friendId);
        friendshipService.addNewFriendshipRequest(userId, friendId);
    }

    // Friendship - Подтверждение заявки на дружбу
    @PatchMapping("/{userId}/confirm/{friendId}")
    public void confirmFriendship(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long friendId) {
        log.info("Подтверждение пользователем с id={} заявки на дружбу с пользователем с id={}", userId, friendId);
        friendshipService.confirmFriendship(userId, friendId);
    }

    // Friendship - Удаление пользователя из своего списка друзей
    @PatchMapping("/{userId}/cancel/{friendId}")
    public void removeFriend(
            @NotNull @PathVariable Long userId,
            @NotNull @PathVariable Long friendId) {
        log.info("Удаление пользователем с id={} пользователя с id={} из своего списка друзей", userId, friendId);
        friendshipService.cancelFriendship(userId, friendId);
    }

    // Friendship - Просмотр списка своих друзей
    @GetMapping("/{userId}")
    public List<UserDto> findFriends(@NotNull @PathVariable Long userId) {
        log.info("Получение списка друзей пользователя с id={}", userId);
        return friendshipService.findFriends(userId);
    }
}
