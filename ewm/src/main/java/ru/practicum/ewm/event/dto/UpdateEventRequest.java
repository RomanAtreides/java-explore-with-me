package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.ewm.utility.marker.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@NotNull(groups = Update.class)
public class UpdateEventRequest {

    @Size(min = 20, max = 2000, groups = Update.class)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000, groups = Update.class)
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(groups = Update.class)
    LocalDateTime eventDate;

    Long eventId;

    @Value("false")
    boolean paid;

    @Value("0")
    Integer participantLimit;

    @Size(min = 3, max = 120, groups = Update.class)
    String title;

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
