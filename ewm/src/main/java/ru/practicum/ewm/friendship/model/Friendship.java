package ru.practicum.ewm.friendship.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.friendship.state.FriendshipStatus;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "friendship")
@IdClass(FriendshipId.class)
public class Friendship {

    @Column(name = "friendship_status", nullable = false)
    @Enumerated(EnumType.STRING)
    FriendshipStatus status; // Статус заявки на дружбу

    @Id
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester; // Создатель заявки

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    User friend; // Потенциальный друг

    @Column(name = "created", nullable = false)
    LocalDateTime created; // Дата создания заявки

    @Column(name = "changed")
    LocalDateTime changed; // Дата изменения заявки

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Friendship other = (Friendship) obj;
        return requester != null && friend != null
                && requester.equals(other.requester) && friend.equals(other.requester);
    }

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                ", status=" + status +
                ", requester_id=" + requester.getId() +
                ", friend_id=" + friend.getId() +
                ", created=" + created +
                ", changed=" + changed +
                '}';
    }
}
