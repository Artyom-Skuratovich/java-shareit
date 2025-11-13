package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(long userId, NewItemDto dto);

    ItemDto update(long userId, long itemId, UpdateItemDto dto);

    ItemFullDto find(long userId, long itemId);

    List<ItemFullDto> findAll(long userId);

    List<ItemDto> search(long userId, String text);

    CommentDto addComment(long userId, long itemId, NewCommentDto dto);
}