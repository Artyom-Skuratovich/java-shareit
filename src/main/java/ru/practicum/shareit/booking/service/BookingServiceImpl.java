package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.common.exception.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto create(long userId, BookingRequest request) {
        throwIfInvalidBookingDates(request.getStart(), request.getEnd());
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        final Item item = itemRepository.findById(request.getItemId()).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с id='%d' не найден", request.getItemId()))
        );
        if (item.getOwner().getId() == userId) {
            throw new SelfBookingException("Владелец вещи не может забронировать её же");
        }
        if (!item.isAvailable()) {
            throw new ItemUnavailableException(String.format("Предмет с id='%d' недоступен", item.getId()));
        }
        final Booking saved = bookingRepository.save(BookingMapper.mapToBooking(request, user, item));
        return BookingMapper.mapToDto(saved);
    }

    @Transactional
    @Override
    public BookingDto handleBookingRequest(long userId, long bookingId, boolean approved) {
        final Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(String.format("Запрос на бронирование с id='%d' не найден", bookingId))
        );
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ForbiddenOperationException(String.format(
                    "Подтвердить/отклонить запрос нельзя, так как его статус - %s, допустимый статус - %s",
                    booking.getStatus().name(),
                    BookingStatus.WAITING.name()
            ));
        }
        final Item item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenOperationException("Только владелец предмета может одобрить/отклонить запрос");
        }
        item.setAvailable(!approved);
        itemRepository.save(item);
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public BookingDto find(long userId, long bookingId) {
        final Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(String.format("Бронирование с id='%d' не найдено", bookingId))
        );
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new ForbiddenOperationException("Операция доступна только владельцу вещи и автору бронирования");
        }
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public List<BookingDto> findUserBookings(long userId, State state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        final LocalDateTime now = LocalDateTime.now();
        final List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByBookerId(userId);
            case CURRENT -> bookingRepository.findCurrentBookingsByBookerId(userId, now);
            case PAST -> bookingRepository.findPastBookingsByBookerId(userId, now);
            case FUTURE -> bookingRepository.findFutureBookingsByBookerId(userId, now);
            case WAITING, REJECTED ->
                    bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.valueOf(state.name()));
        };
        return bookings.stream().map(BookingMapper::mapToDto).toList();
    }

    @Override
    public List<BookingDto> findUserItemsBookings(long userId, State state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        final LocalDateTime now = LocalDateTime.now();
        final List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByOwnerId(userId);
            case CURRENT -> bookingRepository.findCurrentBookingsByOwnerId(userId, now);
            case PAST -> bookingRepository.findPastBookingsByOwnerId(userId, now);
            case FUTURE -> bookingRepository.findFutureBookingsByOwnerId(userId, now);
            case WAITING, REJECTED ->
                    bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.valueOf(state.name()));
        };
        return bookings.stream().map(BookingMapper::mapToDto).toList();
    }

    private static void throwIfInvalidBookingDates(LocalDateTime start, LocalDateTime end) {
        final LocalDateTime now = LocalDateTime.now();
        if (start.isBefore(now)) {
            throw new InvalidBookingDateException("Дата начала бронирования не может быть в прошлом");
        }
        if (end.isBefore(now)) {
            throw new InvalidBookingDateException("Дата конца бронирования не может быть в прошлом");
        }
        if (!start.isBefore(end)) {
            throw new InvalidBookingDateException("Дата начала бронирования должна быть меньше даты его окончания");
        }
    }
}