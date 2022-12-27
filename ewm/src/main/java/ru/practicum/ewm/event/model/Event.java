package ru.practicum.ewm.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.EventState;
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
    private Long id; // Идентификатор

    @Column(name = "annotation")
    private String annotation; // Краткое описание

    @ManyToOne // Много событий может быть с одинаковой категорией
    @JoinColumn(name = "category_id")
    private Category category; // Категория; id + name
    // private CategoryDto category; // Категория; id + name

    @Column(name = "confirmed_requests")
    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии

    @Column(name = "created_on")
    private LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss"); example: 2022-09-06 11:00:23

    @Column(name = "description")
    private String description; // Полное описание события

    @Column(name = "event_date")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss"); example: 2024-12-31 15:10:05

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator; // Пользователь (краткая информация); id + name!
    // private UserShortDto initiator; // Пользователь (краткая информация); id + name!

    // С.Савельев - Вебинар по архитектуре, часть 1:
    // Лучше сделать это именно в рамках таблицы events, просто добавить 2 столбца, долгота и широта
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "latitude")), // Широта
            @AttributeOverride(name = "lon", column = @Column(name = "longitude")) // Долгота
    })
    private Location location; // Широта и долгота места проведения события; lat + lon

    @Column(name = "paid")
    private boolean paid; // Нужно ли оплачивать участие

    @Column(name = "participant_limit")
    private Integer participantLimit; // default: 0; Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @Column(name = "published_on")
    private LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss"); example: 2022-09-06 15:10:05

    @Column(name = "request_moderation")
    private boolean requestModeration; // default: true; Нужна ли пре-модерация заявок на участие

    @Enumerated(EnumType.STRING)
    private EventState state; // Список состояний жизненного цикла события; example: PUBLISHED; Enum: [ PENDING, PUBLISHED, CANCELED ]

    @Column(name = "title")
    private String title; // Заголовок; example: Знаменитое шоу 'Летающая кукуруза'

    @Column(name = "views")
    private Long views; // Количество просмотрев события
}
