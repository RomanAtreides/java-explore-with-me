package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserAdminService {

    UserDto addNewUser(UserDto userDto);

    List<UserDto> findUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
