package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

public final class BookingMapper {
    private BookingMapper() {
    }

    public static Booking mapToBooking(BookingRequest request, User booker, Item item) {
        Booking booking = new Booking();
        booking.setStart(request.getStart());
        booking.setEnd(request.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public static BookingDto mapToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        dto.setBooker(UserMapper.mapToSimpleDto(booking.getBooker()));
        dto.setItem(ItemMapper.mapToSimpleDto(booking.getItem()));
        return dto;
    }
}