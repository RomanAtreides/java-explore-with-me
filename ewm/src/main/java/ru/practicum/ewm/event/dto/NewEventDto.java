package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.utility.marker.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NotNull(groups = Create.class)
public class NewEventDto {

    @Size(min = 20, max = 2000, groups = Create.class)
    @NotNull(groups = Create.class)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000, groups = Create.class)
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(groups = Create.class)
    LocalDateTime eventDate;

    Location location;

    @Value("false")
    boolean paid;

    @Value("0")
    Integer participantLimit;

    @Value("true")
    boolean requestModeration;

    @Size(min = 3, max = 120, groups = Create.class)
    String title;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "annotation length='" + annotation.length() + '\'' +
                ", category=" + category +
                ", description length='" + (description != null ? description.length() : 0) + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                '}';
    }
}
