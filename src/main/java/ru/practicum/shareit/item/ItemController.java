package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item create(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemDto item) {
        return itemService.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item update(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody UpdateItemDto item) {
        return itemService.update(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public Item find(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        return itemService.find(userId, itemId);
    }

    @GetMapping
    public List<Item> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public List<Item> search(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam String text) {
        return itemService.search(userId, text);
    }
}