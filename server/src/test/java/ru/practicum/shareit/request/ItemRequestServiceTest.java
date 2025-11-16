package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final ItemRequestServiceImpl itemRequestService;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    private User user;
    private User requestor;
    private ItemRequest itemRequest;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        user = userRepository.save(user);

        requestor = new User();
        requestor.setName("Requestor");
        requestor.setEmail("requestor@example.com");
        requestor = userRepository.save(requestor);

        itemRequest = new ItemRequest();
        itemRequest.setDescription("Test request");
        itemRequest.setRequestor(requestor);
        itemRequest = itemRequestRepository.save(itemRequest);
    }

    @Test
    public void createShouldSaveRequestAndReturnDto() {
        NewItemRequestDto newDto = new NewItemRequestDto();
        newDto.setDescription("New request");

        ItemRequestDto result = itemRequestService.create(user.getId(), newDto);

        assertNotNull(result);
        assertEquals("New request", result.getDescription());
    }

    @Test
    public void findAllByUserShouldReturnList() {
        List<ItemRequestItemsDto> list = itemRequestService.findAllByUser(requestor.getId());

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(itemRequest.getId(), list.getFirst().getId());
    }

    @Test
    public void findRequestsOfOthersShouldReturnList() {
        List<ItemRequestItemsDto> list = itemRequestService.findRequestsOfOthers(user.getId());

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(itemRequest.getId(), list.getFirst().getId());
    }

    @Test
    public void findShouldReturnRequestDto() {
        ItemRequestItemsDto dto = itemRequestService.find(requestor.getId(), itemRequest.getId());


        assertNotNull(dto);
        assertEquals(itemRequest.getId(), dto.getId());
        assertEquals(itemRequest.getDescription(), dto.getDescription());
    }

    @Test
    public void createShouldThrowWhenUserNotFound() {
        assertThrows(NotFoundException.class, () ->
                itemRequestService.create(999L, new NewItemRequestDto()));
    }

    @Test
    public void findShouldThrowWhenRequestNotFound() {
        assertThrows(NotFoundException.class, () ->
                itemRequestService.find(user.getId(), 999L));
    }
}