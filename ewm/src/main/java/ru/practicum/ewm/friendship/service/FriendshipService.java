package ru.practicum.ewm.friendship.service;

import ru.practicum.ewm.friendship.dto.FriendshipDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface FriendshipService {

    FriendshipDto addNewFriendshipRequest(Long userId, Long friendId);

    void confirmFriendship(Long userId, Long friendId);

    void cancelFriendship(Long userId, Long friendId);

    List<UserDto> findFriends(Long userId);
}
