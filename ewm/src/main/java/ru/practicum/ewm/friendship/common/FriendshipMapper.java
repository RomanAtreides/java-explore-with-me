package ru.practicum.ewm.friendship.common;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.friendship.dto.FriendshipDto;
import ru.practicum.ewm.friendship.model.Friendship;

@Component
public class FriendshipMapper {

    public static FriendshipDto toFriendshipDto(Friendship friendship) {
        return FriendshipDto.builder()
                .status(friendship.getStatus())
                .requesterId(friendship.getRequester().getId())
                .friendId(friendship.getFriend().getId())
                .created(friendship.getCreated())
                .changed(friendship.getChanged())
                .build();
    }
}
