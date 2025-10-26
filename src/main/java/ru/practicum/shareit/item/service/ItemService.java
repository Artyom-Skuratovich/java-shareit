package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(long userId, ItemDto dto);

    ItemDto update(long userId, long itemId, UpdateItemDto dto);

    ItemDto find(long userId, long itemId);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(long userId, String text);
}