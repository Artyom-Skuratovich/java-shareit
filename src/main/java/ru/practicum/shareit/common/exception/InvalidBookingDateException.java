package ru.practicum.shareit.common.exception;

public class InvalidBookingDateException extends RuntimeException {
    public InvalidBookingDateException(String message) {
        super(message);
    }
}