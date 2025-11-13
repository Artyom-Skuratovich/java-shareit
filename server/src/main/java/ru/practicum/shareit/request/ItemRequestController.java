package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody NewItemRequestDto itemRequest) {
        return itemRequestService.create(userId, itemRequest);
    }

    @GetMapping
    public List<ItemRequestItemsDto> findAllByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.findAllByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestItemsDto> findRequestsOfOthers(@RequestHeader("X-Sharer-User-Id") long excludedUserId) {
        return itemRequestService.findRequestsOfOthers(excludedUserId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestItemsDto find(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId) {
        return itemRequestService.find(userId, requestId);
    }
}