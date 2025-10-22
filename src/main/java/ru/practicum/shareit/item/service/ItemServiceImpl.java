package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Override
    public Item create(long userId, ItemDto item) {
        return null;
    }

    @Override
    public Item update(long userId, long itemId, UpdateItemDto item) {
        return null;
    }

    @Override
    public Item find(long userId, long itemId) {
        return null;
    }

    @Override
    public List<Item> findAll(long userId) {
        return List.of();
    }

    @Override
    public List<Item> search(long userId, String text) {
        return List.of();
    }
}