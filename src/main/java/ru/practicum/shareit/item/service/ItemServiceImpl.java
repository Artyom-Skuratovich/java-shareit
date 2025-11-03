package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemDto create(long userId, ItemDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        Item item = ItemMapper.mapToItem(dto);
        item.setOwner(user);
        return ItemMapper.mapToDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(long userId, long itemId, UpdateItemDto dto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
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
    public ItemDto find(long userId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с id='%d' не найден", itemId))
        );
        return ItemMapper.mapToDto(item);
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        return itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::mapToDto).toList();
    }

    @Override
    public List<ItemDto> search(long userId, String text) {
        return itemRepository.search(text).stream().map(ItemMapper::mapToDto).toList();
    }
}