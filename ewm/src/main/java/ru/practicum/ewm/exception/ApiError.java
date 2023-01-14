package ru.practicum.ewm.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ApiError {

    List<StackTraceElement> errors;

    String message;

    String reason;

    String status;

    String timestamp;
}
