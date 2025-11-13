package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.SimpleItemDto;
import ru.practicum.shareit.user.dto.SimpleUserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private SimpleUserDto booker;
    private SimpleItemDto item;
    private BookingStatus status;
}