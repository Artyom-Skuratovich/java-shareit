package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.SimpleBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingMapperTest {
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        item = new Item();
        item.setId(2L);
        item.setName("Test Item");
    }

    @Test
    void mapToBookingShouldCreateValidBooking() {
        // Arrange
        BookingRequest request = new BookingRequest();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        request.setStart(start);
        request.setEnd(end);

        // Act
        Booking booking = BookingMapper.mapToBooking(request, user, item);

        // Assert
        assertNotNull(booking);
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(user, booking.getBooker());
        assertEquals(item, booking.getItem());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void mapToDtoShouldReturnCorrectDto() {
        // Arrange
        final Booking booking = getBooking();

        // Act
        BookingDto dto = BookingMapper.mapToDto(booking);

        // Assert
        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(BookingStatus.APPROVED, dto.getStatus());

        assertNotNull(dto.getBooker());
        assertEquals(5L, dto.getBooker().getId());

        assertNotNull(dto.getItem());
        assertEquals(3L, dto.getItem().getId());
    }

    @Test
    void mapToSimpleDtoShouldReturnCorrectSimpleDto() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(7L);
        booking.setStart(LocalDateTime.of(2023, 1, 1, 10, 0));
        booking.setEnd(LocalDateTime.of(2023, 1, 2, 10, 0));

        User booker = new User();
        booker.setId(8L);
        booking.setBooker(booker);

        // Act
        SimpleBookingDto simpleDto = BookingMapper.mapToSimpleDto(booking);

        // Assert
        assertNotNull(simpleDto);
        assertEquals(7L, simpleDto.getId());
        assertEquals(booking.getStart(), simpleDto.getStart());
        assertEquals(booking.getEnd(), simpleDto.getEnd());
        assertEquals(8L, simpleDto.getBookerId());
    }

    private static Booking getBooking() {
        final Booking booking = new Booking();
        booking.setId(10L);
        booking.setStart(LocalDateTime.of(2023, 1, 1, 10, 0));
        booking.setEnd(LocalDateTime.of(2023, 1, 2, 10, 0));
        booking.setStatus(BookingStatus.APPROVED);

        User booker = new User();
        booker.setId(5L);
        booking.setBooker(booker);

        Item item = new Item();
        item.setId(3L);
        booking.setItem(item);
        return booking;
    }
}