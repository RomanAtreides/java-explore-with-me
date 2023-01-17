package ru.practicum.ewm.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.friendship.common.FriendshipMapper;
import ru.practicum.ewm.friendship.dto.FriendshipDto;
import ru.practicum.ewm.user.common.UserMapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.friendship.model.Friendship;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.friendship.repository.FriendshipRepository;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.friendship.state.FriendshipStatus;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    private final EntityManager entityManager;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Override
    public FriendshipDto addNewFriendshipRequest(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя создать заявку на дружбу с самим собой");
        }

        List<User> users = userRepository.findUsers(new Long[]{userId, friendId});

        if (users == null || users.size() < 2) {
            throw new EntityNotFoundException(
                    "Для создания заявки на дружбу оба пользователя должны быть зарегистрированы в системе"
            );
        }

        if (userId > friendId) {
            users = users.stream()
                    .sorted((u1, u2) -> u2.getId().compareTo(u1.getId()))
                    .collect(Collectors.toList());
        }

        User user = users.get(0);
        User friend = users.get(1);

        Friendship friendship = friendshipRepository.findByRequesterIdAndFriendId(userId, friendId);

        if (friendship != null) {
            throw new ValidationException(String.format(
                    "Заявка не дружбу между пользователями с id=%d и id=%d уже существует", userId, friendId
            ));
        }

        friendship = Friendship.builder()
                .id(null)
                .status(FriendshipStatus.PENDING)
                .requester(user)
                .friend(friend)
                .created(LocalDateTime.now())
                .changed(null)
                .build();

        Friendship entity = friendshipRepository.save(friendship);

        return FriendshipMapper.toFriendshipDto(entity);
    }

    @Override
    public void confirmFriendship(Long userId, Long friendId) {
        Friendship friendship = getFriendshipIfExists(userId, friendId);

        if (friendship.getStatus().equals(FriendshipStatus.CONFIRMED)) {
            throw new ValidationException(String.format(
                    "Заявка на дружбу между пользователями с id=%d и id=%d уже подтверждена", userId, friendId
            ));
        }

        if (friendship.getRequester().getId().equals(userId)) {
            throw new ValidationException("Подтвердить заявку на дружбу может только потенциальный друг");
        }

        if (friendship.getStatus().equals(FriendshipStatus.CANCELED)) {
            throw new ValidationException(String.format(
                    "Заявка на дружбу между пользователями с id=%d и id=%d была отменена ранее", userId, friendId
            ));
        }
        friendship.setStatus(FriendshipStatus.CONFIRMED);
        friendship.setChanged(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    @Override
    public void cancelFriendship(Long userId, Long friendId) {
        Friendship friendship = getFriendshipIfExists(userId, friendId);

        if (friendship.getStatus().equals(FriendshipStatus.CANCELED)) {
            throw new ValidationException(String.format(
                    "Заявка на дружбу пользователей с id=%d и id=%d уже была отменена", userId, friendId
            ));
        }
        friendship.setStatus(FriendshipStatus.CANCELED);
        friendship.setChanged(LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findFriends(Long userId) {
        Query queryToGetFriends = entityManager.createNativeQuery(
                "SELECT * FROM users " +
                        "WHERE id IN (SELECT requester_id " +
                        "FROM friendship " +
                        "WHERE friend_id = ?1 " +
                        "AND friendship_status = ?2 " +
                        "UNION " +
                        "SELECT friend_id " +
                        "FROM friendship " +
                        "WHERE requester_id = ?1 " +
                        "AND friendship_status = ?2)", User.class
        );

        List<?> users = queryToGetFriends
                .setParameter(1, userId)
                .setParameter(2, String.valueOf(FriendshipStatus.CONFIRMED))
                .getResultList();

        return users.stream()
                .map(u -> UserMapper.toUserDto((User) u))
                .collect(Collectors.toList());
    }

    private Friendship getFriendshipIfExists(Long userId, Long friendId) {
        Friendship friendship = friendshipRepository.findByRequesterIdAndFriendId(userId, friendId);

        if (friendship == null) {
            throw new EntityNotFoundException(String.format(
                    "Заявка на дружбу между пользователями с id=%d и id=%d не найдена", userId, friendId
            ));
        }
        return friendship;
    }
}
