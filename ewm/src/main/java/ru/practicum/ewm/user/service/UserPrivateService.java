package ru.practicum.ewm.user.service;

public interface UserPrivateService {

    void addNewFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);
}
