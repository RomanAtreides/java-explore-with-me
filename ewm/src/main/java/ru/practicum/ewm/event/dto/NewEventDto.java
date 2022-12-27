package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.utility.marker.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Новое событие
public class NewEventDto {

    @Size(min = 20, max = 2000, groups = Create.class)
    private String annotation; // Краткое описание; maxLength: 2000, minLength: 20; example: Сплав на байдарках похож на полет

    private Long category; // id категории к которой относится событие

    /*@Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryForNewEventDto {

        private Long id;
    }*/

    @Size(min = 20, max = 7000, groups = Create.class)
    private String description; // Полное описание события; maxLength: 7000, minLength: 20

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(groups = Create.class)
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss"); example: 2024-12-31 15:10:05

    private Location location; // Широта и долгота места проведения события; lat + lon

    @Value("false")
    private boolean paid; // Нужно ли оплачивать участие; default: false

    @Value("0")
    private Integer participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения; default: 0

    @Value("true")
    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие; default: true
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически;

    @Size(min = 3, max = 120, groups = Create.class)
    private String title; // Заголовок события; example: Сплав на байдарках; maxLength: 120, minLength: 3

    @Override
    public String toString() {
        return "NewEventDto{" +
                "annotation length='" + annotation.length() + '\'' +
                ", category=" + category +
                ", description length='" + description.length() + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                '}';
    }
}
