package ru.practicum.ewm.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

// Админ:   события
    // get      /admin/events - Поиск событий
    // put      /admin/events/{eventId} - Редактирование события
    // patch    /admin/events/{eventId}/publish - Публикация события
    // patch    /admin/events/{eventId}/reject - Отклонение события

// Админ:   категории
    // patch    /admin/categories - Изменение категории
    // post     /admin/categories - Добавление новой категории
    // delete   /admin/categories/{catId} - Удаление категории

// Админ:   пользователи
    // get      /admin/users - Получение информации о пользователях

    // post     /admin/users - Добавление нового пользователя
    @PostMapping("/users")
    public UserDto addNewUser(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.addNewUser(userDto);
        log.info("Добавление нового пользователя {}", newUserDto);
        return newUserDto;
    }

    // delete   /admin/users/{userId} - Удаление пользователя

// Админ:   Подборки событий
    // post     /admin/compilations - Добавление новой подборки
    // delete   /admin/compilations/{compId} - Удаление подборки
    // delete   /admin/compilations/{compId}/events/{eventId} - Удалить событие из подборки
    // patch    /admin/compilations/{compId}/events/{eventId} - Добавить событие в подборку
    // delete   /admin/compilations/{compId}/pin - Открепить подборку на главной странице
    // patch    /admin/compilations/{compId}/pin - Закрепить подборку на главной странице
}
