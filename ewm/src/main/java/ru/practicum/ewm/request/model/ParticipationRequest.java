package ru.practicum.ewm.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.ParticipationStatus;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Идентификатор заявки

    @Column(name = "created")
    private LocalDateTime created; // Дата и время создания заявки; example: 2022-09-06T21:10:05.432

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; // Идентификатор события

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; // Идентификатор пользователя, отправившего заявку

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status; // Статус заявки

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
