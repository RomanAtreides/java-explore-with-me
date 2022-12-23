package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    public final UserService userService;

    // Admin: Users - Получение информации о пользователях
    @GetMapping
    public List<UserDto> findAllUsers(
            @RequestParam(required = false) Long[] ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение списка всех пользователей");
        return userService.findAllUsers(ids, from, size);
    }

    // Admin: Users - Добавление нового пользователя
    @PostMapping
    public UserDto addNewUser(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.addNewUser(userDto);
        log.info("Добавление нового пользователя {}", newUserDto);
        return newUserDto;
    }

    // Admin: Users - Удаление пользователя
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя с id = {}", userId);
        userService.deleteUser(userId);
    }

    // public:
    // events
    // compilations
    // categories

    // private:
    // events
    // requests

    // admin:
    // events
    // categories
    // users
    // compilations

    // total entities:
    // categories
    // compilations
    // events
    // requests
    // users

// Админ:   события
    // get      /admin/events - Поиск событий
    // put      /admin/events/{eventId} - Редактирование события
    // patch    /admin/events/{eventId}/publish - Публикация события
    // patch    /admin/events/{eventId}/reject - Отклонение события

// Админ:   категории
    // patch    /admin/categories - Изменение категории
    // post     /admin/categories - Добавление новой категории
    // delete   /admin/categories/{catId} - Удаление категории

// Админ:   Подборки событий
    // post     /admin/compilations - Добавление новой подборки
    // delete   /admin/compilations/{compId} - Удаление подборки
    // delete   /admin/compilations/{compId}/events/{eventId} - Удалить событие из подборки
    // patch    /admin/compilations/{compId}/events/{eventId} - Добавить событие в подборку
    // delete   /admin/compilations/{compId}/pin - Открепить подборку на главной странице
    // patch    /admin/compilations/{compId}/pin - Закрепить подборку на главной странице
}
