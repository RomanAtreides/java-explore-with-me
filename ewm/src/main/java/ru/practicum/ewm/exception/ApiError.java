package ru.practicum.ewm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    private List<StackTraceElement> errors; // Список стектрейсов или описания ошибок, example: List []
    private String message; // Сообщение об ошибке, example: Only pending or canceled events can be changed
    private String reason; // Общее описание причины ошибки, example: For the requested operation the conditions are not met.
    private String status; // Код статуса HTTP-ответа, example: FORBIDDEN
    private String timestamp; // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")

    /*
     * При валидации может быть несколько ошибок
     */
}
