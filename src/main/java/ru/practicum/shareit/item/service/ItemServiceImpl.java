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

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final InMemoryUserRepository userRepository;
    private final InMemoryItemRepository itemRepository;

    @Override
    public ItemDto create(long userId, ItemDto dto) {
        User user = userRepository.find(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        Item item = itemRepository.create(ItemMapper.mapToItem(dto), user);
        return ItemMapper.mapToDto(item);
    }

    @Override
    public ItemDto update(long userId, long itemId, UpdateItemDto dto) {
        Item item = itemRepository.find(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с id='%d' не найден", itemId))
        );
        ItemMapper.updateItemProperties(item, dto);
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException(
                    String.format("Предмет с id='%d' не принадлежит пользователю с id='%d'", itemId, userId)
            );
        }
        return ItemMapper.mapToDto(itemRepository.update(item));
    }

    @Override
    public ItemDto find(long userId, long itemId) {
        Item item = itemRepository.find(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с id='%d' не найден", itemId))
        );
        return ItemMapper.mapToDto(item);
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        return itemRepository.findAll(userId).stream().map(ItemMapper::mapToDto).toList();
    }

    @Override
    public List<ItemDto> search(long userId, String text) {
        return itemRepository.search(text).stream().map(ItemMapper::mapToDto).toList();
    }
}