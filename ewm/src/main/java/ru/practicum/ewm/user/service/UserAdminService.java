package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserAdminService {

    UserDto addNewUser(NewUserRequest newUserRequest);

    List<UserDto> findUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
