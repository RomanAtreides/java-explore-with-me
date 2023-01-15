package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserPrivateService {

    void addNewFriend(Long id, Long friendId);

    void cancelFriendship(Long id, Long friendId);

    void confirmFriendship(Long id, Long friendId);

    List<UserDto> findFriends(Long id);
}
