package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserAdminService;
import ru.practicum.ewm.utility.marker.Create;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserAdminService userService;

    // Admin: Users - Получение информации о пользователях
    @GetMapping
    public List<UserDto> findUsers(
            @RequestParam(required = false) Long[] ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение списка пользователей from={}, size={} или с ids={}", from, size, ids);
        return userService.findUsers(ids, from, size);
    }

    // Admin: Users - Добавление нового пользователя
    @PostMapping
    public UserDto addNewUser(@RequestBody @Validated(Create.class) NewUserRequest newUserRequest) {
        UserDto newUserDto = userService.addNewUser(newUserRequest);

        log.info("Добавление нового пользователя {}", newUserDto);
        return newUserDto;
    }

    // Admin: Users - Удаление пользователя
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя с id={}", userId);
        userService.deleteUser(userId);
    }
}
