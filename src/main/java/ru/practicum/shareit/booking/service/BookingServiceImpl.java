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
import ru.practicum.shareit.shared.exception.InvalidBookingDateException;
import ru.practicum.shareit.shared.exception.ItemUnavailableException;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.shared.exception.NotOwnerApprovalException;
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
        final Item item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new NotOwnerApprovalException("Только владелец предмета может одобрить/отклонить запрос");
        }
        item.setAvailable(!approved);
        itemRepository.save(item);
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.mapToDto(booking);
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

    private static void throwIfInvalidBookingDates(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();
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