package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.annotation.ValidBookingDates;

import java.time.LocalDateTime;

@ValidBookingDates
@Data
public class BookingRequest {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}