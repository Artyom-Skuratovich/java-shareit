package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.exception.ForbiddenOperationException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    private User user;
    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        user = userRepository.save(user);

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        item = new Item();
        item.setName("Item");
        item.setOwner(owner);
        item.setAvailable(true);
        item = itemRepository.save(item);

        booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking = bookingRepository.save(booking);
    }

    @Test
    public void createShouldCreateBookingWhenValidData() {
        BookingRequest request = new BookingRequest();
        request.setItemId(item.getId());
        request.setStart(LocalDateTime.now().plusHours(1));
        request.setEnd(LocalDateTime.now().plusHours(2));

        BookingDto result = bookingService.create(user.getId(), request);

        assertNotNull(result);
        assertEquals(user.getId(), result.getBooker().getId());
        assertEquals(item.getId(), result.getItem().getId());
    }

    @Test
    public void handleBookingRequestShouldApprove() {
        bookingService.handleBookingRequest(owner.getId(), booking.getId(), true);
        Booking updated = bookingRepository.findById(booking.getId()).orElseThrow();
        assertEquals(BookingStatus.APPROVED, updated.getStatus());
        assertFalse(updated.getItem().isAvailable());
    }

    @Test
    public void findShouldReturnBookingWhenOwnerOrBooker() {
        BookingDto dtoOwner = bookingService.find(owner.getId(), booking.getId());
        assertNotNull(dtoOwner);

        BookingDto dtoBooker = bookingService.find(user.getId(), booking.getId());
        assertNotNull(dtoBooker);
    }

    @Test
    public void findUserBookingsShouldReturnList() {
        booking.setStart(LocalDateTime.now());
        List<BookingDto> result = bookingService.findUserBookings(user.getId(), State.CURRENT);
        assertFalse(result.isEmpty());
    }

    @Test
    void findUserBookingsShouldReturnFutureBookingsWhenStateIsFUTURE() {
        userRepository.save(user);
        LocalDateTime now = LocalDateTime.now();
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setStart(now.plusDays(1));
        booking.setEnd(now.plusDays(5));
        bookingRepository.save(booking);
        List<BookingDto> result = bookingService.findUserBookings(user.getId(), State.FUTURE);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findUserBookingsShouldReturnAllByStatusWhenStateIsWAITING() {
        userRepository.save(user);
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        List<BookingDto> result = bookingService.findUserBookings(user.getId(), State.WAITING);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findUserBookingsShouldThrowNotFoundExceptionWhenUserNotExists() {
        long userId = 7L;
        assertThrows(NotFoundException.class, () -> {
            bookingService.findUserBookings(userId, State.ALL);
        });
    }

    @Test
    void findAllUserBookings() {
        User booker = new User();
        booker.setName("name");
        booker.setEmail("qwerty@mail.ru");

        booker = userRepository.save(booker);

        Booking booking1 = new Booking();
        booking1.setBooker(booker);
        booking1.setStatus(BookingStatus.REJECTED);
        booking1.setItem(item);
        booking1.setStart(LocalDateTime.now());
        booking1.setEnd(booking1.getStart().plusDays(1));

        Booking booking2 = new Booking();
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.REJECTED);
        booking2.setItem(item);
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(booking2.getStart().plusDays(1));

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        assertEquals(2, bookingService.findUserBookings(booker.getId(), State.ALL).size());
    }

    @Test
    void findPastUserBookings() {
        User booker = new User();
        booker.setName("name");
        booker.setEmail("qwerty@mail.ru");

        booker = userRepository.save(booker);

        Booking booking1 = new Booking();
        booking1.setBooker(booker);
        booking1.setStatus(BookingStatus.REJECTED);
        booking1.setItem(item);
        booking1.setStart(LocalDateTime.now().minusDays(5));
        booking1.setEnd(booking1.getStart().plusDays(1));

        Booking booking2 = new Booking();
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.REJECTED);
        booking2.setItem(item);
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(booking2.getStart().plusDays(1));

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        assertEquals(1, bookingService.findUserBookings(booker.getId(), State.PAST).size());
    }

    @Test
    public void handleBookingRequestShouldThrowForbiddenOperationExceptionWhenStatusIsREJECTED() {
        booking.setStatus(BookingStatus.REJECTED);
        assertThrows(ForbiddenOperationException.class, () ->
                bookingService.handleBookingRequest(owner.getId(), booking.getId(), true)
        );
    }
}