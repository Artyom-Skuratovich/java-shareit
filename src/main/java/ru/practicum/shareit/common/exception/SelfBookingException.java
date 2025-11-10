package ru.practicum.shareit.common.exception;

public class SelfBookingException extends RuntimeException {
    public SelfBookingException(String message) {
        super(message);
    }
}