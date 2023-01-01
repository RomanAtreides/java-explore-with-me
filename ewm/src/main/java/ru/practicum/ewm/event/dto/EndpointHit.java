package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHit {

    private Long id;

    private String app; // Идентификатор сервиса для которого записывается информация

    private String uri; // URI для которого был осуществлен запрос

    private String ip; // IP-адрес пользователя, осуществившего запрос

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // Дата и время, когда был совершен запрос к ручке (в формате "yyyy-MM-dd HH:mm:ss")
}
