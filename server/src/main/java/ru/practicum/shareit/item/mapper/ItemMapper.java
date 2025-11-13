package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.SimpleBookingDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Objects;

public final class ItemMapper {
    private ItemMapper() {
    }

    public static Item updateItemProperties(Item item, UpdateItemDto dto) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return item;
    }

    public static Item mapToItem(NewItemDto dto, ItemRequest request) {
        final Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setRequest(request);
        return item;
    }

    public static ItemDto mapToDto(Item item) {
        final ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setOwnerId(item.getOwner().getId());
        return dto;
    }

    public static SimpleItemDto mapToSimpleDto(Item item) {
        Objects.requireNonNull(item);
        final SimpleItemDto dto = new SimpleItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        return dto;
    }

    public static ItemFullDto mapToFullDto(
            Item item,
            SimpleBookingDto last,
            SimpleBookingDto next,
            List<CommentDto> comments) {
        final ItemFullDto dto = new ItemFullDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setOwnerId(item.getOwner().getId());
        dto.setLastBooking(last);
        dto.setNextBooking(next);
        dto.setComments(comments);
        return dto;
    }

    public static ItemRequestResponseDto mapToItemResponseDto(Item item) {
        final ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwner().getId());
        return dto;
    }
}