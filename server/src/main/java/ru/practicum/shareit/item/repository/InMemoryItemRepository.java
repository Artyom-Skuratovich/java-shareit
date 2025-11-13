package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Deprecated
@Component
public class InMemoryItemRepository {
    private static long id = 1;
    private final HashMap<Long, Item> itemMap;

    public InMemoryItemRepository() {
        itemMap = new HashMap<>();
    }

    public List<Item> findAll(long userId) {
        return itemMap.values().stream().filter(i -> i.getOwner().getId() == userId).toList();
    }

    public Optional<Item> find(long itemId) {
        return Optional.ofNullable(itemMap.get(itemId));
    }

    public Item create(Item item, User owner) {
        item.setId(nextId());
        item.setOwner(owner);
        itemMap.put(item.getId(), item);
        return item;
    }

    public Item update(Item item) {
        itemMap.put(item.getId(), item);
        return item;
    }

    public List<Item> search(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        final String pattern = text.toLowerCase();
        return itemMap.values()
                .stream()
                .filter(
                        i -> i.isAvailable() &&
                                (i.getName().toLowerCase().contains(pattern) ||
                                        i.getDescription().toLowerCase().contains(pattern)))
                .toList();
    }

    private static synchronized long nextId() {
        return id++;
    }
}