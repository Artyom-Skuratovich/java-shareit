package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.UserHasNoBookingsException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDto create(long userId, ItemDto dto) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        final Item item = ItemMapper.mapToItem(dto);
        item.setOwner(user);
        return ItemMapper.mapToDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(long userId, long itemId, UpdateItemDto dto) {
        final Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с id='%d' не найден", itemId))
        );
        ItemMapper.updateItemProperties(item, dto);
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException(
                    String.format("Предмет с id='%d' не принадлежит пользователю с id='%d'", itemId, userId)
            );
        }
        return ItemMapper.mapToDto(itemRepository.save(item));
    }

    @Override
    public ItemFullDto find(long userId, long itemId) {
        final Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с id='%d' не найден", itemId))
        );
        return toFullDto(item, LocalDateTime.now());
    }

    @Override
    public List<ItemFullDto> findAll(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        final LocalDateTime now = LocalDateTime.now();
        return itemRepository.findAllByOwnerId(userId).stream().map(i -> toFullDto(i, now)).toList();
    }

    @Override
    public List<ItemDto> search(long userId, String text) {
        return text.isBlank() ?
                List.of() :
                itemRepository.search(text).stream().map(ItemMapper::mapToDto).toList();
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, long itemId, NewCommentDto dto) {
        final User author = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        final Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с id='%d' не найден", itemId))
        );
        if (!bookingRepository.existsPastBookingByBookerId(userId, itemId, LocalDateTime.now())) {
            throw new UserHasNoBookingsException(String.format(
                    "У пользователя с id='%d' нет завершённых бронирований предмета с id='%d'",
                    userId,
                    itemId
            ));
        }
        final Comment saved = commentRepository.save(CommentMapper.mapToComment(dto, author, item));
        return CommentMapper.mapToDto(saved);
    }

    private ItemFullDto toFullDto(Item item, LocalDateTime date) {
        final Booking last = bookingRepository.findPastBookingsByItemId(
                item.getId(), date, PageRequest.of(0, 1)
        ).stream().findFirst().orElse(null);
        final Booking next = bookingRepository.findFutureBookingsByItemId(
                item.getId(), date, PageRequest.of(0, 1)
        ).stream().findFirst().orElse(null);
        return ItemMapper.mapToFullDto(
                item,
                last != null ? BookingMapper.mapToSimpleDto(last) : null,
                next != null ? BookingMapper.mapToSimpleDto(next) : null,
                commentRepository.findCommentsByItemId(item.getId()).stream().map(CommentMapper::mapToDto).toList()
        );
    }
}