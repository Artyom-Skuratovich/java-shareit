package ru.practicum.shareit.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Violation {
    private final String property;
    private final String message;
}