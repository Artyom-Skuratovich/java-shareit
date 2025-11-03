package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public BookingDto create(long userId, BookingRequest request) {
        return null;
    }

    @Transactional
    @Override
    public BookingDto handleBookingRequest(long userId, long bookingId, boolean approved) {
        return null;
    }

    @Override
    public BookingDto find(long userId, long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> findUserBookings(long userId, State state) {
        return List.of();
    }

    @Override
    public List<BookingDto> findUserItemsBookings(long userId, State state) {
        return List.of();
    }
}