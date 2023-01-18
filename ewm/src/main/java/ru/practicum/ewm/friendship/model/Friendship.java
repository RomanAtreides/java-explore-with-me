package ru.practicum.ewm.friendship.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.friendship.state.FriendshipStatus;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "friendship")
@IdClass(FriendshipId.class)
public class Friendship {

    @Column(name = "friendship_status", nullable = false)
    @Enumerated(EnumType.STRING)
    FriendshipStatus status;

    @Id
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    User friend;

    @Column(name = "created", nullable = false)
    LocalDateTime created;

    @Column(name = "changed")
    LocalDateTime changed;

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                ", status=" + status +
                ", requester id=" + requester.getId() +
                ", requester id=" + friend.getId() +
                ", created=" + created +
                ", created=" + changed +
                '}';
    }
}
