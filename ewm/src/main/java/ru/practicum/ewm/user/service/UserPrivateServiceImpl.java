package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.common.UserValidator;
import ru.practicum.ewm.user.model.Friendship;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.FriendshipRepository;
import ru.practicum.ewm.user.state.FriendshipStatus;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPrivateServiceImpl implements UserPrivateService {

    private final FriendshipRepository friendshipRepository;
    private final UserValidator userValidator;

    @Override
    public void addNewFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("Нельзя создать заявку на дружбу с самим собой");
        }

        User user = userValidator.getUserIfExists(id);
        User friend = userValidator.getUserIfExists(friendId);

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
    public void removeFriend(Long id, Long friendId) {
        Friendship friendship = friendshipRepository.findByRequesterIdAndFriendId(id, friendId);

        if (friendship == null) {
            throw new EntityNotFoundException(String.format(
                    "Пользователи с id=%d и id=%d не состоят в дружеских отношения", id, friendId
            ));
        }

        if (!friendship.getStatus().equals(FriendshipStatus.CONFIRMED)) {
            throw new ValidationException(String.format(
                    "Нельзя удалить из друзей пользователя с id=%d не находящегося в списке друзей", friendId
            ));
        }
        friendship.setStatus(FriendshipStatus.CANCELED);
        friendshipRepository.save(friendship);
    }
}
