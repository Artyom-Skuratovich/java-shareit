package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Valid @RequestBody ItemDto item) {
        return itemService.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @Valid @RequestBody UpdateItemDto item) {
        return itemService.update(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto find(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        return itemService.find(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam String text) {
        return itemService.search(userId, text);
    }
}