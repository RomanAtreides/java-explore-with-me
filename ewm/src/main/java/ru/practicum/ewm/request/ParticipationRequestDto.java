package ru.practicum.ewm.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.ParticipationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Заявка на участие в событии
public class ParticipationRequestDto {

    private Long id; // Идентификатор заявки
    private String created; // Дата и время создания заявки; example: 2022-09-06T21:10:05.432
    private Long event; // Идентификатор события
    private Long requester; // Идентификатор пользователя, отправившего заявку
    private ParticipationStatus status; // Статус заявки
}
