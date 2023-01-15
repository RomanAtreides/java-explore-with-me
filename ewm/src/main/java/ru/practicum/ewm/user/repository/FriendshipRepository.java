package ru.practicum.ewm.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.Friendship;
import ru.practicum.ewm.user.state.FriendshipStatus;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("select f from Friendship f where f.requester.id in (?1, ?2) and f.friend.id in (?1, ?2)")
    Friendship findByRequesterIdAndFriendId(Long id, Long friendId);

    @Query("select f.friend.id from Friendship f where f.requester.id = ?1 and f.status = ?2")
    Long[] findFriends(Long id, FriendshipStatus status);
}
