package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestMapperTest {

    @Test
    void mapToItemRequestShouldMapFieldsCorrectly() {
        NewItemRequestDto dto = new NewItemRequestDto();
        dto.setDescription("Test description");

        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("John");

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(dto, requestor);

        assertEquals(dto.getDescription(), itemRequest.getDescription());
        assertNotNull(itemRequest.getCreated());
        assertEquals(requestor, itemRequest.getRequestor());
        assertTrue(itemRequest.getCreated().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void mapToDtoShouldMapFieldsCorrectly() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(10L);
        itemRequest.setDescription("Request desc");
        itemRequest.setCreated(LocalDateTime.of(2023, 10, 1, 12, 0));

        ItemRequestDto dto = ItemRequestMapper.mapToDto(itemRequest);

        assertEquals(itemRequest.getId(), dto.getId());
        assertEquals(itemRequest.getDescription(), dto.getDescription());
        assertEquals(itemRequest.getCreated(), dto.getCreated());
    }

    @Test
    void mapToDtoWithResponsesShouldMapFieldsAndItems() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(5L);
        itemRequest.setDescription("Description");
        itemRequest.setCreated(LocalDateTime.of(2023, 10, 1, 12, 0));

        User owner = new User();
        owner.setId(1L);
        owner.setName("Artyom");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item2");
        item2.setOwner(owner);

        List<Item> items = List.of(item1, item2);

        ItemRequestItemsDto dto = ItemRequestMapper.mapToDtoWithResponses(itemRequest, items);

        assertEquals(itemRequest.getId(), dto.getId());
        assertEquals(itemRequest.getDescription(), dto.getDescription());
        assertEquals(itemRequest.getCreated(), dto.getCreated());
        assertEquals(2, dto.getItems().size());
    }
}