package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {

    private String annotation; // Краткое описание
    private CategoryDto category; // Категория; id + name
    private Long confirmedRequests; // Количество одобренных заявок на участие в данном событии

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss"); example: 2022-09-06 11:00:23

    private String description; // Полное описание события

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss"); example: 2024-12-31 15:10:05

    private Long id; // Идентификатор
    private UserShortDto initiator; // Пользователь (краткая информация); id + name!
    private Location location; // Широта и долгота места проведения события; lat + lon
    private boolean paid; // Нужно ли оплачивать участие
    private Integer participantLimit; // default: 0; Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss"); example: 2022-09-06 15:10:05

    private boolean requestModeration; // default: true; Нужна ли пре-модерация заявок на участие
    private EventState state; // Список состояний жизненного цикла события; example: PUBLISHED; Enum: [ PENDING, PUBLISHED, CANCELED ]
    private String title; // Заголовок; example: Знаменитое шоу 'Летающая кукуруза'
    private Long views; // Количество просмотрев события

    /*
     *  С.Савельев - вебинар по архитектуре - 1 часть:
     *  confirmedRequests - количество одобренных заявок на участие в данном событии
     *  1. Не нужно для каждого события делать отдельный запрос в БД
     *  2. Нужно сделать один запрос сразу для всех событий с помощью оператора IN и уже из ответа сгуруппировать по
     *  событиям эти заявки
     *
     *  Также не стоит забывать про QueryDSL для динамических запросов (когда мы на этапе
     *  написания кода не знаем, по каким полям будет фильтрация)
     */

}
