package ru.practicum.shareit.shared.exception;

public class InvalidBookingDateException extends RuntimeException {
    public InvalidBookingDateException(String message) {
        super(message);
    }
}