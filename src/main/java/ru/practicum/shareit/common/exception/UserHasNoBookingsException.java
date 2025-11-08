package ru.practicum.shareit.common.exception;

public class UserHasNoBookingsException extends RuntimeException {
    public UserHasNoBookingsException(String message) {
        super(message);
    }
}