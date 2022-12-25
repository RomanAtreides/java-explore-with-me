package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.model.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Новое событие
public class NewEventDto {

    private String annotation; // Краткое описание; maxLength: 2000, minLength: 20; example: Сплав на байдарках похож на полет
    private Long category; // id категории к которой относится событие
    private String description; // Полное описание события; maxLength: 7000, minLength: 20
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss"); example: 2024-12-31 15:10:05
    private Location location; // Широта и долгота места проведения события; lat + lon
    private boolean paid; // Нужно ли оплачивать участие; default: false
    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения; default: 0
    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически; default: true
    private String title; // Заголовок события; example: Сплав на байдарках; maxLength: 120, minLength: 3
}
