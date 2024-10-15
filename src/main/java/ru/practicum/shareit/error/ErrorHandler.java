package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exception.AccessException;
import ru.practicum.shareit.error.exception.ConflictException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidateException;

import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_BLUE;
import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_HIGH_YELLOW;
import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_PURPLE;
import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_RED;
import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_RESET;
import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_YELLOW;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.error("{}{}{}", ANSI_BLUE, e.getMessage(), ANSI_RESET);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ValidateException.class,
            MissingRequestHeaderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception e) {
        log.error("{}{}{}", ANSI_YELLOW, e.getMessage(), ANSI_RESET);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList()
                .toString();
        log.error("{}{}{}", ANSI_YELLOW, errors, ANSI_RESET);
        return new ErrorResponse(errors);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException e) {
        log.error("{}{}{}", ANSI_PURPLE, e.getMessage(), ANSI_RESET);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(AccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessException(AccessException e) {
        log.error("{}{}{}", ANSI_HIGH_YELLOW, e.getMessage(), ANSI_RESET);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNotKnownException(Exception e) {
        log.error("{}{}{}", ANSI_RED, e.getMessage(), ANSI_RESET);
        return new ErrorResponse(e.getMessage());
    }
}