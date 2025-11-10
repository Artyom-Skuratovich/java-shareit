package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleBookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long bookerId;
}