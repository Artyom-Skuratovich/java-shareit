package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.State;

import java.util.List;

public interface BookingService {
    BookingDto create(long userId, BookingRequest request);

    BookingDto handleBookingRequest(long userId, long bookingId, boolean approved);

    BookingDto find(long userId, long bookingId);

    List<BookingDto> findUserBookings(long userId, State state);

    List<BookingDto> findUserItemsBookings(long userId, State state);
}