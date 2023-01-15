package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.service.UserPrivateService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/private/users")
public class UserPrivateController {

    private final String addOrRemoveFriendUrlPart = "{id}/friendship/{friendId}";
    private final UserPrivateService userPrivateService;

    // Private: Users - Просмотр списка своих друзей
    /*@GetMapping
    public List<UserDto> findFriends(
            @RequestParam(required = false) Long[] ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение списка пользователей from={}, size={} или с ids={}", from, size, ids);
        return userService.findUsers(ids, from, size);
    }*/

    // Private: Users - Создание заявки на добавление пользователя в свой список друзей
    @PostMapping(addOrRemoveFriendUrlPart)
    public void addNewFriend(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        log.info("Добавление пользователем с id={} пользователя с id={} в свой список друзей", id, friendId);
        userPrivateService.addNewFriend(id, friendId);
    }

    // Private: Users - Отмена своей заявки на добавление пользователя в свой список друзей

    // Private: Users - Подтверждение заявки на добавление пользователя в свой список друзей

    // Private: Users - Удаление пользователя из своего списка друзей
    @DeleteMapping(addOrRemoveFriendUrlPart)
    public void removeFriend(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        log.info("Удаление пользователем с id={} пользователя с id={} из своего списка друзей", id, friendId);
        userPrivateService.removeFriend(id, friendId);
    }
}
