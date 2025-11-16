package ru.practicum.shareit.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.common.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void handleItemUnavailableException_ReturnsCorrectResponse() {
        String message = "Item is not available";
        ItemUnavailableException ex = new ItemUnavailableException(message);
        ResponseEntity<ErrorResponse> response = handler.handleItemUnavailableException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getError());
    }

    @Test
    public void handleUserHasNoBookingsException_ReturnsCorrectResponse() {
        String message = "User has no bookings";
        UserHasNoBookingsException ex = new UserHasNoBookingsException(message);
        ResponseEntity<ErrorResponse> response = handler.handleUserHasNoBookingsException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getError());
    }

    @Test
    public void handleSelfBookingException_ReturnsCorrectResponse() {
        String message = "Self booking not allowed";
        SelfBookingException ex = new SelfBookingException(message);
        ResponseEntity<ErrorResponse> response = handler.handleSelfBookingException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getError());
    }

    @Test
    public void handleNotOwnerApprovalException_ReturnsCorrectResponse() {
        String message = "Operation forbidden";
        ForbiddenOperationException ex = new ForbiddenOperationException(message);
        ResponseEntity<ErrorResponse> response = handler.handleNotOwnerApprovalException(ex);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getError());
    }

    @Test
    public void handleNotFoundException_ReturnsCorrectResponse() {
        String message = "Item not found";
        NotFoundException ex = new NotFoundException(message);
        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getError());
    }

    @Test
    public void handleSameEmailException_ReturnsCorrectResponse() {
        String message = "Emails must be different";
        SameEmailException ex = new SameEmailException(message);
        ResponseEntity<ErrorResponse> response = handler.handleSameEmailException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getError());
    }
}