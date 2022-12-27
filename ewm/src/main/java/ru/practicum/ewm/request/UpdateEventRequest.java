package ru.practicum.ewm.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.ewm.utility.marker.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull(groups = Update.class)
// Данные для изменения информации о событии
public class UpdateEventRequest {

    @Size(min = 20, max = 2000, groups = Update.class)
    private String annotation; // Новая аннотация; maxLength: 2000, minLength: 20

    private Long category; // Новая категория; example: 3

    @Size(min = 20, max = 7000, groups = Update.class)
    private String description; // Новое описание; maxLength: 7000, minLength: 20

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Future(groups = Update.class)
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss"); example: 2024-12-31 15:10:05

    private Long eventId; // Идентификатор события; example: 1

    @Value("false")
    private boolean paid; // Новое значение флага о платности мероприятия

    @Value("0")
    private Integer participantLimit; // Новый лимит пользователей. Значение 0 - означает отсутствие ограничения; default: 0

    @Size(min = 3, max = 120, groups = Update.class)
    private String title; // Новый заголовок; example: Сплав на байдарках; maxLength: 120, minLength: 3

    @Override
    public String toString() {
        return "UpdateEventRequest{" +
                "annotation length='" + annotation.length() + '\'' +
                ", category=" + category +
                ", description length='" + description.length() + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventId=" + eventId +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", title='" + title + '\'' +
                '}';
    }
}
