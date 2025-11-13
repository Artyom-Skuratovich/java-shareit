package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerId(long bookerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.booker.id = ?1 AND " +
            "b.start > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByBookerId(long bookerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.booker.id = ?1 AND " +
            "b.end < ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByBookerId(long bookerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.booker.id = ?1 AND " +
            "b.start <= ?2 AND b.end >= ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByBookerId(long bookerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.booker.id = ?1 AND " +
            "b.status = ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdAndStatus(long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(long ownerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.start > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByOwnerId(long ownerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.end < ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByOwnerId(long ownerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.start <= ?2 AND b.end >= ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByOwnerId(long ownerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.status = ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStatus(long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.id = ?1 AND " +
            "b.end >= ?2 AND b.start <= ?2 AND " +
            "b.status IN (?3) " +
            "ORDER BY b.end DESC")
    Page<Booking> findPastBookingsByItemId(
            long itemId,
            LocalDateTime date,
            Set<BookingStatus> includedStatuses,
            Pageable pageable);

    @Query("SELECT b from Booking b " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.id = ?1 AND b.start > ?2 AND " +
            "b.status IN (?3) " +
            "ORDER BY b.start ASC")
    Page<Booking> findFutureBookingsByItemId(
            long itemId,
            LocalDateTime date,
            Set<BookingStatus> includedStatuses,
            Pageable pageable);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.item.id = ?2 AND " +
            "b.end < ?3 AND " +
            "b.status IN (?4)")
    boolean existsPastBookingByBookerId(
            long bookerId,
            long itemId,
            LocalDateTime date,
            Set<BookingStatus> includedStatuses);
}