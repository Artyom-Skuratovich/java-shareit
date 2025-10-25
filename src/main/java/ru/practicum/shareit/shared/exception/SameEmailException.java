package ru.practicum.shareit.shared.exception;

public class SameEmailException extends RuntimeException {
    public SameEmailException(String message) {
        super(message);
    }
}