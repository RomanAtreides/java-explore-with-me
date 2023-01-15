package ru.practicum.ewm.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.user.state.FriendshipStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "friendship")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "friendship_status")
    @Enumerated(EnumType.STRING)
    FriendshipStatus status;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    User friend;

    @Column(name = "created")
    LocalDateTime created;

    @Column(name = "received")
    LocalDateTime received;

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                "id=" + id +
                ", status=" + status +
                ", requester id=" + requester.getId() +
                ", requester id=" + friend.getId() +
                ", created=" + created +
                ", created=" + received +
                '}';
    }
}
