package ru.practicum.shareit.common.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private final List<Violation> violations;

    public ValidationErrorResponse(String error, List<Violation> violations) {
        super(error);
        this.violations = violations;
    }
}