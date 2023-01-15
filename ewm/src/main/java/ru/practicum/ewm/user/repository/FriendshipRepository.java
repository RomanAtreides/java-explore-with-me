package ru.practicum.ewm.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.user.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Friendship findByRequesterIdAndFriendId(Long id, Long friendId);
}
