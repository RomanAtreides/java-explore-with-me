package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserPrivateService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/private/users")
public class UserPrivateController {

    private final UserPrivateService userPrivateService;

    // Private: Users - Просмотр списка своих друзей
    @GetMapping("{id}/friendship")
    public List<UserDto> findFriends(
            @NotNull @PathVariable Long id) {
        log.info("Получение списка друзей пользователя с id={}", id);
        return userPrivateService.findFriends(id);
    }

    // Private: Users - Создание заявки на добавление пользователя в свой список друзей
    @PostMapping("{id}/friendship/{friendId}/add")
    public void addNewFriend(
            @NotNull @PathVariable Long id,
            @NotNull @PathVariable Long friendId) {
        log.info("Добавление пользователем с id={} пользователя с id={} в свой список друзей", id, friendId);
        userPrivateService.addNewFriend(id, friendId);
    }

    // Private: Users - Подтверждение заявки на добавление пользователя в свой список друзей
    @PatchMapping("{id}/friendship/{friendId}/confirm")
    public void confirmFriendship(
            @NotNull @PathVariable Long id,
            @NotNull @PathVariable Long friendId) {
        log.info("Подтверждение пользователем с id={} заявки на дружбу с пользователем с id={}", id, friendId);
        userPrivateService.confirmFriendship(id, friendId);
    }

    // Private: Users - Удаление пользователя из своего списка друзей
    @PatchMapping("{id}/friendship/{friendId}/cancel")
    public void removeFriend(
            @NotNull @PathVariable Long id,
            @NotNull @PathVariable Long friendId) {
        log.info("Удаление пользователем с id={} пользователя с id={} из своего списка друзей", id, friendId);
        userPrivateService.cancelFriendship(id, friendId);
    }
}
