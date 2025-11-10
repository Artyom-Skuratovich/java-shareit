package ru.practicum.shareit.common.exception.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private final List<String> details;

    public ValidationErrorResponse(String message, List<String> details) {
        super(message);
        this.details = details;
    }
}