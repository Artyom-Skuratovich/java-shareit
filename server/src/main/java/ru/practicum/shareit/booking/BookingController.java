package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Valid @RequestBody BookingRequest request) {
        return bookingService.create(userId, request);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto handleBookingRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId,
            @RequestParam boolean approved) {
        return bookingService.handleBookingRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto find(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId) {
        return bookingService.find(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findUserBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL", required = false) State state) {
        return bookingService.findUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findUserItemsBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL", required = false) State state) {
        return bookingService.findUserItemsBookings(userId, state);
    }
}