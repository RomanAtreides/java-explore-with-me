package ru.practicum.ewm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    private List<StackTraceElement> errors;

    private String message;

    private String reason;

    private String status;

    private String timestamp;
}
