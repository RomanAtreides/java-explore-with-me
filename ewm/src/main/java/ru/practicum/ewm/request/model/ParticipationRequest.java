package ru.practicum.ewm.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.state.ParticipationStatus;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "created")
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Enumerated(EnumType.STRING)
    ParticipationStatus status;

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
        ParticipationRequest other = (ParticipationRequest) obj;
        return id != null && id.equals(other.getId());
    }

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                "id=" + id +
                ", created=" + created +
                ", event id=" + event.getId() +
                ", requester id=" + requester.getId() +
                ", status=" + status +
                '}';
    }
}
