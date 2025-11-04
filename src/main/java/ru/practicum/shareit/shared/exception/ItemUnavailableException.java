package ru.practicum.shareit.shared.exception;

public class ItemUnavailableException extends RuntimeException {
    public ItemUnavailableException(String message) {
        super(message);
    }
}