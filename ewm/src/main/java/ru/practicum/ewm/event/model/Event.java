package ru.practicum.ewm.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.state.EventState;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "annotation")
    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "confirmed_requests")
    Long confirmedRequests;

    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(name = "description")
    String description;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "latitude")),
            @AttributeOverride(name = "lon", column = @Column(name = "longitude"))
    })
    Location location;

    @Column(name = "paid")
    boolean paid;

    @Column(name = "participant_limit")
    Integer participantLimit;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Enumerated(EnumType.STRING)
    EventState state;

    @Column(name = "title", nullable = false, unique = true)
    String title;

    @Column(name = "views")
    Long views;

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        return Objects.equals(title, other.title);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", annotation_length='" + (annotation != null ? annotation.length() : "0") + '\'' +
                ", category_name=" + category.getName() +
                ", confirmedRequests=" + confirmedRequests +
                ", createdOn=" + createdOn +
                ", description_length='" + (description != null ? description.length() : "0") + '\'' +
                ", eventDate=" + eventDate +
                ", initiator_email=" + (initiator != null ? initiator.getEmail() : "empty") +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", publishedOn=" + publishedOn +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}
