package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.utility.DateTimeForm;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice("ru.practicum.ewm")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException exception) {
        log.debug("400 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                "Неверный запрос",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(DateTimeForm.FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessException(final AccessException exception) {
        log.debug("403 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                "Ошибка доступа",
                HttpStatus.FORBIDDEN.toString(),
                LocalDateTime.now().format(DateTimeForm.FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException exception) {
        log.debug("404 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                "Требуемый объект не найден",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().format(DateTimeForm.FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestConflictException(final SQLException exception) {
        log.debug("409 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                "Конфликт данных",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().format(DateTimeForm.FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleEntityAlreadyExistsException(final EntityAlreadyExistsException exception) {
        log.debug("500 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                "Ошибка сервера",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().format(DateTimeForm.FORMATTER)
        );
    }
}
