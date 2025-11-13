package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        User owner = new User();
        owner.setName("Owner");
        owner = entityManager.persistAndFlush(owner);

        User booker = new User();
        booker.setName("Booker");
        booker = entityManager.persistAndFlush(booker);

        Item item = new Item();
        item.setName("Test Item");
        item.setOwner(owner);
        item = entityManager.persistAndFlush(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        booking = entityManager.persistAndFlush(booking);
    }

    private User createUser(String name) {
        User user = new User();
        user.setName(name);
        return entityManager.persistFlushFind(user);
    }

    private Item createItem(User owner, String name) {
        Item item = new Item();
        item.setOwner(owner);
        item.setName(name);
        return entityManager.persistFlushFind(item);
    }

    private void createBooking(User booker, Item item, LocalDateTime start, LocalDateTime end, BookingStatus status) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        entityManager.persistFlushFind(booking);
    }

    @Test
    void testFindAllByBookerId() {
        User user = createUser("User1");
        Item item = createItem(user, "Item1");
        User booker = createUser("Booker");
        createBooking(booker, item, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), BookingStatus.APPROVED);

        List<Booking> bookings = bookingRepository.findAllByBookerId(booker.getId());
        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getBooker().getId()).isEqualTo(booker.getId());
    }

    @Test
    void testFindFutureBookingsByBookerId() {
        User booker = createUser("Booker");
        User owner = createUser("Owner");
        Item item = createItem(owner, "Item");
        // Бронирование в будущем
        createBooking(booker, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), BookingStatus.WAITING);

        List<Booking> futureBookings = bookingRepository.findFutureBookingsByBookerId(booker.getId(), LocalDateTime.now());
        assertThat(futureBookings).hasSize(1);
        assertThat(futureBookings.getFirst().getStart()).isAfter(LocalDateTime.now());
    }

    @Test
    void testFindPastBookingsByBookerId() {
        User booker = createUser("Booker");
        User owner = createUser("Owner");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1), BookingStatus.REJECTED);

        List<Booking> pastBookings = bookingRepository.findPastBookingsByBookerId(booker.getId(), LocalDateTime.now());
        assertThat(pastBookings).hasSize(1);
        assertThat(pastBookings.getFirst().getEnd()).isBefore(LocalDateTime.now());
    }

    @Test
    void testFindCurrentBookingsByBookerId() {
        User booker = createUser("Booker");
        User owner = createUser("Owner");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), BookingStatus.APPROVED);

        List<Booking> currentBookings = bookingRepository.findCurrentBookingsByBookerId(booker.getId(), LocalDateTime.now());
        assertThat(currentBookings).hasSize(1);
        assertThat(currentBookings.getFirst().getStart()).isBefore(LocalDateTime.now());
        assertThat(currentBookings.getFirst().getEnd()).isAfter(LocalDateTime.now());
    }

    @Test
    void testFindAllByBookerIdAndStatus() {
        User booker = createUser("Booker");
        User owner = createUser("Owner");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now(), LocalDateTime.now().plusDays(1), BookingStatus.WAITING);

        var bookings = bookingRepository.findAllByBookerIdAndStatus(booker.getId(), BookingStatus.WAITING);
        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testFindAllByOwnerId() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Owner's Item");
        createBooking(booker, item, LocalDateTime.now(), LocalDateTime.now().plusDays(1), BookingStatus.APPROVED);

        var bookings = bookingRepository.findAllByOwnerId(owner.getId());
        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getItem().getOwner().getId()).isEqualTo(owner.getId());
    }

    // Аналогичные тесты для методов с фильтрацией по владельцу

    @Test
    void testFindFutureBookingsByOwnerId() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3), BookingStatus.WAITING);

        var bookings = bookingRepository.findFutureBookingsByOwnerId(owner.getId(), LocalDateTime.now());
        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStart()).isAfter(LocalDateTime.now());
    }

    @Test
    void testFindPastBookingsByOwnerId() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1), BookingStatus.REJECTED);

        var bookings = bookingRepository.findPastBookingsByOwnerId(owner.getId(), LocalDateTime.now());
        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getEnd()).isBefore(LocalDateTime.now());
    }

    @Test
    void testFindCurrentBookingsByOwnerId() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), BookingStatus.APPROVED);

        var bookings = bookingRepository.findCurrentBookingsByOwnerId(owner.getId(), LocalDateTime.now());
        assertThat(bookings).hasSize(1);
    }

    @Test
    void testFindAllByOwnerIdAndStatus() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now(), LocalDateTime.now().plusDays(1), BookingStatus.WAITING);

        var bookings = bookingRepository.findAllByOwnerIdAndStatus(owner.getId(), BookingStatus.WAITING);
        assertThat(bookings).hasSize(1);
    }

    @Test
    void testFindLastBookingsByItemId() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(1), BookingStatus.APPROVED);

        Pageable pageable = PageRequest.of(0, 10);
        var page = bookingRepository.findLastBookingsByItemId(item.getId(), LocalDateTime.now(), Collections.singleton(BookingStatus.APPROVED), pageable);
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    void testFindFutureBookingsByItemId() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3), BookingStatus.WAITING);

        Pageable pageable = PageRequest.of(0, 10);
        var page = bookingRepository.findFutureBookingsByItemId(item.getId(), LocalDateTime.now(), Collections.singleton(BookingStatus.WAITING), pageable);
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    void testExistsPastBookingByBookerId() {
        User owner = createUser("Owner");
        User booker = createUser("Booker");
        Item item = createItem(owner, "Item");
        createBooking(booker, item, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1), BookingStatus.APPROVED);

        boolean exists = bookingRepository.existsPastBookingByBookerId(
                booker.getId(),
                item.getId(),
                LocalDateTime.now(),
                Collections.singleton(BookingStatus.APPROVED)
        );
        assertThat(exists).isTrue();
    }
}