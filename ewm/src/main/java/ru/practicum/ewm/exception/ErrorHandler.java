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
        return ApiError.builder()
                .errors(Arrays.asList(exception.getStackTrace()))
                .message(exception.getMessage())
                .reason("Неверный запрос")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(DateTimeForm.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessException(final AccessException exception) {
        log.debug("403 {}", exception.getMessage(), exception);
        return ApiError.builder()
                .errors(Arrays.asList(exception.getStackTrace()))
                .message(exception.getMessage())
                .reason("Ошибка доступа")
                .status(HttpStatus.FORBIDDEN.toString())
                .timestamp(LocalDateTime.now().format(DateTimeForm.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException exception) {
        log.debug("404 {}", exception.getMessage(), exception);
        return ApiError.builder()
                .errors(Arrays.asList(exception.getStackTrace()))
                .message(exception.getMessage())
                .reason("Требуемый объект не найден")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().format(DateTimeForm.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestConflictException(final SQLException exception) {
        log.debug("409 {}", exception.getMessage(), exception);
        return ApiError.builder()
                .errors(Arrays.asList(exception.getStackTrace()))
                .message(exception.getMessage())
                .reason("Конфликт данных")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now().format(DateTimeForm.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleEntityAlreadyExistsException(final EntityAlreadyExistsException exception) {
        log.debug("500 {}", exception.getMessage(), exception);
        return ApiError.builder()
                .errors(Arrays.asList(exception.getStackTrace()))
                .message(exception.getMessage())
                .reason("Ошибка сервера")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now().format(DateTimeForm.FORMATTER))
                .build();
    }
}
