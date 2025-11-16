package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.SimpleBookingDto;

import java.util.List;

@Data
public class ItemFullDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private SimpleBookingDto lastBooking;
    private SimpleBookingDto nextBooking;
    private List<CommentDto> comments;
}