package ru.practicum.shareit.shared.exception;

public class UserHasNoBookingsException extends RuntimeException {
    public UserHasNoBookingsException(String message) {
        super(message);
    }
}