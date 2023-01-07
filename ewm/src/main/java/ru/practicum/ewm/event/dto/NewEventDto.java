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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {

    @Size(min = 20, max = 2000, groups = Create.class)
    @NotNull(groups = Create.class)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, groups = Create.class)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(groups = Create.class)
    private LocalDateTime eventDate;

    private Location location;

    @Value("false")
    private boolean paid;

    @Value("0")
    private Integer participantLimit;

    @Value("true")
    private boolean requestModeration;

    @Size(min = 3, max = 120, groups = Create.class)
    private String title;

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
