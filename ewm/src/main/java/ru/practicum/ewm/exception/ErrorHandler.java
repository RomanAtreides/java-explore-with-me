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
    public ApiError handleBadRequest(final ValidationException exception) {
        log.debug("400 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbidden(final AccessException exception) {
        log.debug("403 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(),
                HttpStatus.FORBIDDEN.toString(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final EntityNotFoundException exception) {
        log.debug("404 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                "The required object was not found",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final RequestConflictException exception) {
        log.debug("409 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(),
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().format(formatter)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerError(final EntityAlreadyExistsException exception) {
        log.debug("500 {}", exception.getMessage(), exception);
        return new ApiError(
                Arrays.asList(exception.getStackTrace()),
                exception.getMessage(),
                exception.getCause().getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().format(formatter)
        );
    }
}
