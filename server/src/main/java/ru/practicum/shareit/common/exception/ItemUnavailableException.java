package ru.practicum.shareit.common.exception;

public class ItemUnavailableException extends RuntimeException {
    public ItemUnavailableException(String message) {
        super(message);
    }
}