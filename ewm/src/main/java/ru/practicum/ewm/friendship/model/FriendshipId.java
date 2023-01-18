package ru.practicum.ewm.friendship.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Objects;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipId implements Serializable {

    Long requester;

    Long friend;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendshipId)) return false;
        FriendshipId that = (FriendshipId) o;
        return requester.equals(that.requester) && friend.equals(that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, friend);
    }
}
