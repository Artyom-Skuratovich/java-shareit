package ru.practicum.shareit.common;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.common.config.StringToStateConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringToStateConverterTest {
    private final StringToStateConverter converter = new StringToStateConverter();

    @Test
    void convert_ShouldReturnExpectedState_WhenInputIsLowerCase() {
        assertEquals(State.ALL, converter.convert("all"));
        assertEquals(State.CURRENT, converter.convert("current"));
        assertEquals(State.PAST, converter.convert("past"));
        assertEquals(State.FUTURE, converter.convert("future"));
        assertEquals(State.WAITING, converter.convert("waiting"));
        assertEquals(State.REJECTED, converter.convert("rejected"));
    }

    @Test
    void convert_ShouldReturnExpectedState_WhenInputIsMixedCase() {
        assertEquals(State.ALL, converter.convert("All"));
        assertEquals(State.CURRENT, converter.convert("CuRrEnT"));
        assertEquals(State.PAST, converter.convert("pAsT"));
        assertEquals(State.FUTURE, converter.convert("FUTURE"));
        assertEquals(State.WAITING, converter.convert("Waiting"));
        assertEquals(State.REJECTED, converter.convert("ReJeCtEd"));
    }

    @Test
    void convert_ShouldReturnExpectedState_WhenInputIsUpperCase() {
        assertEquals(State.ALL, converter.convert("ALL"));
        assertEquals(State.CURRENT, converter.convert("CURRENT"));
        assertEquals(State.PAST, converter.convert("PAST"));
        assertEquals(State.FUTURE, converter.convert("FUTURE"));
        assertEquals(State.WAITING, converter.convert("WAITING"));
        assertEquals(State.REJECTED, converter.convert("REJECTED"));
    }

    @Test
    void convert_ShouldThrowIllegalArgumentException_WhenInputIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("invalid"));
        assertThrows(IllegalArgumentException.class, () -> converter.convert("123"));
        assertThrows(IllegalArgumentException.class, () -> converter.convert(""));
        assertThrows(NullPointerException.class, () -> converter.convert(null));
    }
}