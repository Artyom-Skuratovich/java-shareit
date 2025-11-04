package ru.practicum.shareit.shared.exception;

public class NotOwnerApprovalException extends RuntimeException {
    public NotOwnerApprovalException(String message) {
        super(message);
    }
}