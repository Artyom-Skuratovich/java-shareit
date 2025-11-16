package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController controller;

    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private ItemRequestDto itemRequestDto;
    private ItemRequestItemsDto itemsDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Request description");

        itemsDto = new ItemRequestItemsDto();
    }

    @Test
    void createShouldReturnCreatedItemRequest() throws Exception {
        when(itemRequestService.create(anyLong(), any(NewItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 42L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new NewItemRequestDto())))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));

        verify(itemRequestService).create(eq(42L), any(NewItemRequestDto.class));
    }

    @Test
    void findAllByUserShouldReturnListOfRequests() throws Exception {
        when(itemRequestService.findAllByUser(anyLong()))
                .thenReturn(List.of(itemsDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemsDto.getId()));

        verify(itemRequestService).findAllByUser(eq(42L));
    }

    @Test
    void findRequestsOfOthersShouldReturnList() throws Exception {
        when(itemRequestService.findRequestsOfOthers(anyLong()))
                .thenReturn(List.of(itemsDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemsDto.getId()));

        verify(itemRequestService).findRequestsOfOthers(eq(42L));
    }

    @Test
    void findShouldReturnSpecificRequest() throws Exception {
        when(itemRequestService.find(anyLong(), eq(10L)))
                .thenReturn(itemsDto);

        mvc.perform(get("/requests/10")
                        .header("X-Sharer-User-Id", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemsDto.getId()));

        verify(itemRequestService).find(eq(42L), eq(10L));
    }
}