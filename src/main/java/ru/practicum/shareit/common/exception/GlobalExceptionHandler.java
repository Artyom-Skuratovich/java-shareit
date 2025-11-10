package ru.practicum.shareit.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.common.exception.response.ErrorResponse;
import ru.practicum.shareit.common.exception.response.ValidationErrorResponse;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<String> details = e.getBindingResult()
                .getFieldErrors()
                .stream().map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        final ErrorResponse response = new ValidationErrorResponse("Ошибка валидации", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidBookingDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBookingDateException(InvalidBookingDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ItemUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleItemUnavailableException(ItemUnavailableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserHasNoBookingsException.class)
    public ResponseEntity<ErrorResponse> handleUserHasNoBookingsException(UserHasNoBookingsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(SelfBookingException.class)
    public ResponseEntity<ErrorResponse> handleSelfBookingException(SelfBookingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ErrorResponse> handleNotOwnerApprovalException(ForbiddenOperationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(SameEmailException.class)
    public ResponseEntity<ErrorResponse> handleSameEmailException(SameEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }
}