package ru.practicum.shareit.common.config;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.booking.dto.State;

public class StringToStateConverter implements Converter<String, State> {
    @Override
    public State convert(String source) {
        return State.valueOf(source.toUpperCase());
    }
}