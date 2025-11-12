package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemRequestResponseDto {
    private long id;
    private String name;
    private long ownerId;
}