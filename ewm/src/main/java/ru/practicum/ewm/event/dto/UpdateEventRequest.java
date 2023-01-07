package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.ewm.utility.marker.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull(groups = Update.class)
public class UpdateEventRequest {

    @Size(min = 20, max = 2000, groups = Update.class)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, groups = Update.class)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(groups = Update.class)
    private LocalDateTime eventDate;

    private Long eventId;

    @Value("false")
    private boolean paid;

    @Value("0")
    private Integer participantLimit;

    @Size(min = 3, max = 120, groups = Update.class)
    private String title;

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
