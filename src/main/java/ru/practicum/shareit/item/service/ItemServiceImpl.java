package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.InMemoryItemRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.InMemoryUserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final InMemoryUserRepository userRepository;
    private final InMemoryItemRepository itemRepository;

    @Override
    public Item create(long userId, ItemDto dto) {
        Optional<User> optional = userRepository.find(userId);
        if (optional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        return itemRepository.create(ItemMapper.mapToItem(dto), optional.get());
    }

    @Override
    public Item update(long userId, long itemId, UpdateItemDto dto) {
        Optional<Item> optional = itemRepository.find(itemId);
        if (optional.isEmpty()) {
            throw new NotFoundException(String.format("Предмет с id='%d' не найден", itemId));
        }
        Item item = ItemMapper.updateItemProperties(optional.get(), dto);
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException(
                    String.format("Предмет с id='%d' не принадлежит пользователю с id='%d'", itemId, userId)
            );
        }
        return itemRepository.update(item);
    }

    @Override
    public Item find(long userId, long itemId) {
        Optional<Item> optional = itemRepository.find(itemId);
        if (optional.isEmpty()) {
            throw new NotFoundException(String.format("Предмет с id='%d' не найден", itemId));
        }
        return optional.get();
    }

    @Override
    public List<Item> findAll(long userId) {
        return itemRepository.findAll(userId);
    }

    @Override
    public List<Item> search(long userId, String text) {
        return itemRepository.search(text);
    }
}