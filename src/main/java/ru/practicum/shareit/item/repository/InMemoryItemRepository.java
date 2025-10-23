package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.shared.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class InMemoryItemRepository {
    private final HashMap<Long, Item> itemMap;
    private final HashMap<Long, Set<Long>> userItemsMap;

    public InMemoryItemRepository() {
        itemMap = new HashMap<>();
        userItemsMap = new HashMap<>();
    }

    public List<Item> findAll(long userId) {
        Set<Long> ids = userItemsMap.get(userId);
        if (ids == null) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }
        return itemMap.values().stream().filter(i -> ids.contains(i.getId())).toList();
    }
}