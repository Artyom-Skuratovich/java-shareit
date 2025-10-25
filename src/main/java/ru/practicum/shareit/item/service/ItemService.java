package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(long userId, ItemDto dto);

    Item update(long userId, long itemId, UpdateItemDto dto);

    Item find(long userId, long itemId);

    List<Item> findAll(long userId);

    List<Item> search(long userId, String text);
}