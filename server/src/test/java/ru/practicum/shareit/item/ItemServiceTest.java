package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.UserHasNoBookingsException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    private User user;
    private User owner;
    private ItemRequest itemRequest;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        user = userRepository.save(user);

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        itemRequest = new ItemRequest();
        itemRequest.setDescription("Request description");
        itemRequest.setRequestor(owner);
        itemRequest = itemRequestRepository.save(itemRequest);

        item = new Item();
        item.setName("Test Item");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        item = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking = bookingRepository.save(booking);
    }

    @Test
    public void createShouldCreateAndReturnDto() {
        NewItemDto newDto = new NewItemDto();
        newDto.setName("New Item");
        newDto.setDescription("Description");
        newDto.setAvailable(true);
        newDto.setRequestId(itemRequest.getId());

        ItemDto result = itemService.create(user.getId(), newDto);

        assertNotNull(result);
        assertEquals("New Item", result.getName());
    }

    @Test
    public void updateShouldUpdateItem() {
        UpdateItemDto updateDto = new UpdateItemDto();
        updateDto.setName("Updated Name");
        updateDto.setAvailable(false);

        ItemDto result = itemService.update(owner.getId(), item.getId(), updateDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }

    @Test
    public void findShouldReturnFullDto() {
        ItemFullDto dto = itemService.find(user.getId(), item.getId());

        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
    }

    @Test
    public void findAllShouldReturnList() {
        List<ItemFullDto> list = itemService.findAll(owner.getId());

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(item.getId(), list.getFirst().getId());
    }

    @Test
    public void searchShouldReturnList() {
        List<ItemDto> result = itemService.search(user.getId(), "Test");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void searchShouldReturnEmptyWhenTextBlank() {
        List<ItemDto> result = itemService.search(user.getId(), "   ");
        assertTrue(result.isEmpty());
    }

    @Test
    public void addCommentShouldCreateComment() {
        NewCommentDto commentDto = new NewCommentDto();
        commentDto.setText("Good item");

        CommentDto comment = itemService.addComment(user.getId(), item.getId(), commentDto);

        assertNotNull(comment);
        assertEquals("Good item", comment.getText());
    }

    @Test
    public void addCommentShouldThrowWhenNoPastBooking() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("newuser@example.com");
        newUser = userRepository.save(newUser);

        NewCommentDto commentDto = new NewCommentDto();
        commentDto.setText("Comment");

        User finalNewUser = newUser;
        assertThrows(UserHasNoBookingsException.class, () ->
                itemService.addComment(finalNewUser.getId(), item.getId(), commentDto));
    }

    @Test
    public void createShouldThrowNotFoundExceptionWhenUserNotFound() {
        NewItemDto newDto = new NewItemDto();
        newDto.setName("Name");
        newDto.setDescription("Description");
        newDto.setAvailable(true);
        long invalidUserId = -1;

        assertThrows(NotFoundException.class, () ->
                itemService.create(invalidUserId, newDto)
        );
    }

    @Test
    public void createShouldThrowNotFoundExceptionWhenRequestNotFound() {
        NewItemDto newDto = new NewItemDto();
        newDto.setName("Name");
        newDto.setDescription("Description");
        newDto.setAvailable(true);
        newDto.setRequestId(99999L); // Несуществующий ID

        assertThrows(NotFoundException.class, () ->
                itemService.create(user.getId(), newDto)
        );
    }

    @Test
    public void updateShouldThrowNotFoundExceptionWhenItemNotFound() {
        UpdateItemDto updateDto = new UpdateItemDto();
        updateDto.setName("Updated");
        long invalidUserId = -1;

        assertThrows(NotFoundException.class, () ->
                itemService.update(invalidUserId, item.getId(), updateDto)
        );
    }

    @Test
    public void findShouldThrowNotFoundExceptionWhenItemNotFound() {
        assertThrows(NotFoundException.class, () ->
                itemService.find(user.getId(), -1)
        );
    }

    @Test
    public void findAllShouldThrowNotFoundExceptionWhenUserNotFound() {
        long invalidUserId = -1;
        assertThrows(NotFoundException.class, () ->
                itemService.findAll(invalidUserId)
        );
    }

    @Test
    public void addCommentShouldThrowWhenItemNotFound() {
        NewCommentDto commentDto = new NewCommentDto();
        commentDto.setText("Comment");

        assertThrows(NotFoundException.class, () ->
                itemService.addComment(user.getId(), -1, commentDto)
        );
    }

    @Test
    public void addCommentShouldThrowWhenUserNotFound() {
        NewCommentDto commentDto = new NewCommentDto();
        commentDto.setText("Comment");
        assertThrows(NotFoundException.class, () ->
                itemService.addComment(-1, item.getId(), commentDto)
        );
    }
}