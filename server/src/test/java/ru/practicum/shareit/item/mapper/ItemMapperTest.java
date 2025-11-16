package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.SimpleBookingDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {

    @Test
    void updateItemPropertiesShouldUpdateOnlyNonNullAndNonBlankFields() {
        Item item = new Item();
        item.setName("Old Name");
        item.setDescription("Old Description");
        item.setAvailable(true);

        UpdateItemDto dto = new UpdateItemDto();
        dto.setName("New Name");
        dto.setDescription(" ");
        dto.setAvailable(false);

        Item result = ItemMapper.updateItemProperties(item, dto);

        assertEquals("New Name", result.getName());
        assertEquals("Old Description", result.getDescription());
        assertFalse(result.isAvailable());
    }

    @Test
    void updateItemPropertiesShouldNotUpdateFieldsWhenNull() {
        Item item = new Item();
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);

        UpdateItemDto dto = new UpdateItemDto();
        dto.setName(null);
        dto.setDescription(null);
        dto.setAvailable(null);

        Item result = ItemMapper.updateItemProperties(item, dto);

        assertEquals("Name", result.getName());
        assertEquals("Description", result.getDescription());
        assertTrue(result.isAvailable());
    }

    @Test
    void updateItemProperties_ShouldUpdateAllFieldsWhenValid() {
        Item item = new Item();
        item.setName("Old Name");
        item.setDescription("Old Description");
        item.setAvailable(true);

        UpdateItemDto dto = new UpdateItemDto();
        dto.setName("New Name");
        dto.setDescription("New Description");
        dto.setAvailable(false);

        Item updatedItem = ItemMapper.updateItemProperties(item, dto);

        assertEquals("New Name", updatedItem.getName());
        assertEquals("New Description", updatedItem.getDescription());
        assertFalse(updatedItem.isAvailable());
    }

    @Test
    void updateItemPropertiesShouldNotUpdateFieldsWhenDtoFieldsAreNull() {
        Item item = new Item();
        item.setName("Existing Name");
        item.setDescription("Existing Description");
        item.setAvailable(true);

        UpdateItemDto dto = new UpdateItemDto();
        dto.setName(null);
        dto.setDescription(null);
        dto.setAvailable(null);

        Item result = ItemMapper.updateItemProperties(item, dto);

        assertEquals("Existing Name", result.getName());
        assertEquals("Existing Description", result.getDescription());
        assertTrue(result.isAvailable());
    }

    @Test
    void updateItemPropertiesShouldNotUpdateFieldsWhenDtoFieldsAreBlank() {
        Item item = new Item();
        item.setName("Old Name");
        item.setDescription("Old Description");
        item.setAvailable(true);

        UpdateItemDto dto = new UpdateItemDto();
        dto.setName(" ");
        dto.setDescription("  ");
        dto.setAvailable(null);

        Item result = ItemMapper.updateItemProperties(item, dto);

        assertEquals("Old Name", result.getName());
        assertEquals("Old Description", result.getDescription());
        assertTrue(result.isAvailable());
    }

    @Test
    void updateItemPropertiesShouldUpdateAvailable() {
        Item item = new Item();
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(false);

        UpdateItemDto dto = new UpdateItemDto();
        dto.setName(null);
        dto.setDescription(null);
        dto.setAvailable(true);

        Item result = ItemMapper.updateItemProperties(item, dto);

        assertTrue(result.isAvailable());
    }

    @Test
    void mapToItemShouldCreateItemWithCorrectValues() {
        NewItemDto dto = new NewItemDto();
        dto.setName("New Item");
        dto.setDescription("Description");
        dto.setAvailable(true);

        ItemRequest request = new ItemRequest();
        request.setId(1L);

        Item item = ItemMapper.mapToItem(dto, request);

        assertNotNull(item);
        assertEquals("New Item", item.getName());
        assertEquals("Description", item.getDescription());
        assertTrue(item.isAvailable());
        assertEquals(request, item.getRequest());
    }

    @Test
    void mapToDtoShouldConvertItemToItemDto() {
        User owner = new User();
        owner.setId(42L);

        Item item = new Item();
        item.setId(1L);
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setOwner(owner);

        ItemDto dto = ItemMapper.mapToDto(item);

        assertEquals(1L, dto.getId());
        assertEquals("ItemName", dto.getName());
        assertEquals("ItemDescription", dto.getDescription());
        assertEquals(42L, dto.getOwnerId());
    }

    @Test
    void mapToSimpleDtoShouldReturnSimpleDtoWithIdAndName() {
        Item item = new Item();
        item.setId(10L);
        item.setName("Simple Name");
        item.setDescription("Desc");
        item.setAvailable(true);

        SimpleItemDto dto = ItemMapper.mapToSimpleDto(item);

        assertEquals(10L, dto.getId());
        assertEquals("Simple Name", dto.getName());
    }

    @Test
    void mapToFullDtoShouldCreateFullDtoWithAllFields() {
        Item item = new Item();
        item.setId(5L);
        item.setName("Full Item");
        item.setDescription("Full Description");
        item.setAvailable(true);

        User owner = new User();
        owner.setId(99L);
        item.setOwner(owner);

        SimpleBookingDto lastBooking = new SimpleBookingDto();
        SimpleBookingDto nextBooking = new SimpleBookingDto();
        CommentDto comment1 = new CommentDto();

        ItemFullDto fullDto = ItemMapper.mapToFullDto(item, lastBooking, nextBooking, List.of(comment1));

        assertEquals(5L, fullDto.getId());
        assertEquals("Full Item", fullDto.getName());
        assertEquals("Full Description", fullDto.getDescription());
        assertEquals(99L, fullDto.getOwnerId());
        assertEquals(lastBooking, fullDto.getLastBooking());
        assertEquals(nextBooking, fullDto.getNextBooking());
        assertEquals(1, fullDto.getComments().size());
        assertEquals(comment1, fullDto.getComments().getFirst());
    }

    @Test
    void mapToResponseDtoShouldCreateDtoWithCorrectFields() {
        Item item = new Item();
        item.setId(7L);
        item.setName("Response Item");

        User owner = new User();
        owner.setId(88L);
        item.setOwner(owner);

        ItemResponseDto dto = ItemMapper.mapToResponseDto(item);

        assertEquals(7L, dto.getId());
        assertEquals("Response Item", dto.getName());
        assertEquals(88L, dto.getOwnerId());
    }

    @Test
    void mapToSimpleDtoShouldThrowNPEWhenItemIsNull() {
        assertThrows(NullPointerException.class, () -> {
            ItemMapper.mapToSimpleDto(null);
        });
    }
}