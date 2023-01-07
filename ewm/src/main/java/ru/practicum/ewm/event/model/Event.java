package ru.practicum.ewm.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.state.EventState;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "latitude")),
            @AttributeOverride(name = "lon", column = @Column(name = "longitude"))
    })
    private Location location;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Long views;
}
