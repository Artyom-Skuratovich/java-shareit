package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Valid @RequestBody NewItemRequestDto itemRequest) {
        return client.create(userId, itemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return client.findAllByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findRequestsOfOthers(@RequestHeader("X-Sharer-User-Id") long excludedUserId) {
        return client.findRequestsOfOthers(excludedUserId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> find(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId) {
        return client.find(userId, requestId);
    }
}