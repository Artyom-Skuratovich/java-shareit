package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(long userId, NewItemRequestDto dto);

    List<ItemRequestItemsDto> findAllByUser(long userId);

    List<ItemRequestItemsDto> findRequestsOfOthers(long excludedUserId);

    ItemRequestItemsDto find(long userId, long requestId);
}