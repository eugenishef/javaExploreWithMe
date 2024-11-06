package ru.practicum.util;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.util.exception.BadRequestException;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.exception.NotFoundException;

import java.time.Instant;
import java.util.List;

import static ru.practicum.config.EWMServiceAppConfig.DATE_TIME_FORMATTER;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult result = methodArgumentNotValidException.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return ApiError.builder()
                .errors(List.of(methodArgumentNotValidException.getStackTrace()))
                .message(methodArgumentNotValidException.getBody().getDetail())
                .reason(fieldErrors.getFirst().getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(DATE_TIME_FORMATTER.format(Instant.now()))
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(ConstraintViolationException constraintViolationException) {
        return ApiError.builder()
                .errors(List.of(constraintViolationException.getStackTrace()))
                .message("Invalid method parameter")
                .reason(constraintViolationException.getLocalizedMessage())
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(DATE_TIME_FORMATTER.format(Instant.now()))
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolation(DataIntegrityViolationException dataIntegrityViolationException) {
        return ApiError.builder()
                .errors(List.of(dataIntegrityViolationException.getStackTrace()))
                .message(dataIntegrityViolationException.getLocalizedMessage())
                .reason("Integrity constraint has been violated")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(DATE_TIME_FORMATTER.format(Instant.now()))
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException notFoundException) {
        return ApiError.builder()
                .errors(List.of(notFoundException.getStackTrace()))
                .message(notFoundException.getMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(DATE_TIME_FORMATTER.format(Instant.now()))
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException conflictException) {
        return ApiError.builder()
                .errors(List.of(conflictException.getStackTrace()))
                .message(conflictException.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(DATE_TIME_FORMATTER.format(Instant.now()))
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException badRequestException) {
        return ApiError.builder()
                .errors(List.of(badRequestException.getStackTrace()))
                .message("Invalid parameter")
                .reason(badRequestException.getMessage())
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(DATE_TIME_FORMATTER.format(Instant.now()))
                .build();
    }
}
