package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice("ru.practicum.ewm")
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException exception) {
        log.debug("400 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(), // TODO: 27.12.2022 Заменить на строку
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter) // TODO: 27.12.2022 Удалить приведение к строке?
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessException(final AccessException exception) {
        log.debug("403 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(), // TODO: 27.12.2022 Заменить на строку
                HttpStatus.FORBIDDEN.toString(),
                LocalDateTime.now().format(formatter) // TODO: 27.12.2022 Удалить приведение к строке?
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException exception) {
        log.debug("404 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                "The required object was not found",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().format(formatter) // TODO: 27.12.2022 Удалить приведение к строке?
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestConflictException(final RequestConflictException exception) {
        log.debug("409 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(), // TODO: 27.12.2022 Заменить на строку
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().format(formatter) // TODO: 27.12.2022 Удалить приведение к строке?
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleEntityAlreadyExistsException(final EntityAlreadyExistsException exception) {
        log.debug("500 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(), // TODO: 27.12.2022 Заменить на строку
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().format(formatter) // TODO: 27.12.2022 Удалить приведение к строке?
        );
    }
}
