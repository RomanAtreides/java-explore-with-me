package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addNewUser(UserDto userDto);

    List<UserDto> findAllUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
