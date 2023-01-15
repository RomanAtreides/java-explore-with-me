package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.common.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.Friendship;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.FriendshipRepository;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.state.FriendshipStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPrivateServiceImpl implements UserPrivateService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Override
    public void addNewFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("Нельзя создать заявку на дружбу с самим собой");
        }

        List<User> users = userRepository.findUsers(new Long[]{id, friendId});

        if (users == null || users.size() < 2) {
            throw new EntityNotFoundException("Пользователь не найден");
        }

        if (id > friendId) {
            users = users.stream()
                    .sorted((u1, u2) -> u2.getId().compareTo(u1.getId()))
                    .collect(Collectors.toList());
        }

        User user = users.get(0);
        User friend = users.get(1);

        Friendship friendship = friendshipRepository.findByRequesterIdAndFriendId(id, friendId);

        if (friendship != null) {
            throw new ValidationException(String.format(
                    "Заявка не дружбу между пользователями с id=%d и id=%d уже существует", id, friendId
            ));
        }

        friendship = Friendship.builder()
                .id(null)
                .status(FriendshipStatus.PENDING)
                .requester(user)
                .friend(friend)
                .created(LocalDateTime.now())
                .received(null)
                .build();

        friendshipRepository.save(friendship);
    }

    @Override
    public void cancelFriendship(Long id, Long friendId) {
        Friendship friendship = getFriendshipIfExists(id, friendId);

        if (friendship.getStatus().equals(FriendshipStatus.CANCELED)) {
            throw new ValidationException(String.format(
                    "Заявка на дружбу пользователей с id=%d и id=%d уже была отменена", id, friendId
            ));
        }
        friendship.setStatus(FriendshipStatus.CANCELED);
        friendship.setReceived(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    @Override
    public void confirmFriendship(Long id, Long friendId) {
        Friendship friendship = getFriendshipIfExists(id, friendId);

        if (friendship.getStatus().equals(FriendshipStatus.CONFIRMED)) {
            throw new ValidationException(String.format(
                    "Заявка на дружбу между пользователями с id=%d и id=%d уже подтверждена", id, friendId
            ));
        }

        if (friendship.getRequester().getId().equals(id)) {
            throw new ValidationException("Подтвердить заявку на дружбу может только потенциальный друг");
        }

        if (friendship.getStatus().equals(FriendshipStatus.CANCELED)) {
            throw new ValidationException(String.format(
                    "Заявка на дружбу между пользователями с id=%d и id=%d была отменена ранее", id, friendId
            ));
        }
        friendship.setStatus(FriendshipStatus.CONFIRMED);
        friendship.setReceived(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findFriends(Long id) {
        Long[] friendIds = friendshipRepository.findFriends(id, FriendshipStatus.CONFIRMED);
        List<User> users = userRepository.findUsers(friendIds);

        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private Friendship getFriendshipIfExists(Long id, Long friendId) {
        Friendship friendship = friendshipRepository.findByRequesterIdAndFriendId(id, friendId);

        if (friendship == null) {
            throw new EntityNotFoundException(String.format(
                    "Заявка на дружбу между пользователями с id=%d и id=%d не найдена", id, friendId
            ));
        }
        return friendship;
    }
}
