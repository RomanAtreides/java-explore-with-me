package ru.practicum.ewm.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.friendship.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("select f from Friendship f where f.requester.id in (?1, ?2) and f.friend.id in (?1, ?2)")
    Friendship findByRequesterIdAndFriendId(Long id, Long friendId);
}
