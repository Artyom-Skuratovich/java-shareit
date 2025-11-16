package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.common.exception.GlobalExceptionHandler;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private NewItemDto newItemDto;
    private ItemDto itemDto;
    private UpdateItemDto updateItemDto;
    private ItemFullDto itemFullDto;
    private NewCommentDto newCommentDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()) // если нужен глобальный обработчик ошибок
                .build();
    }

    private void initialize() {
        newItemDto = new NewItemDto();
        newItemDto.setName("NewItemDto");
        newItemDto.setDescription("New Item Dto");

        itemDto = new ItemDto();

        updateItemDto = new UpdateItemDto();
        updateItemDto.setName("UpdateItemDto");
        updateItemDto.setDescription("Update Item Dto");

        itemFullDto = new ItemFullDto();

        newCommentDto = new NewCommentDto();
        newCommentDto.setText("Test comment");

        commentDto = new CommentDto();
        commentDto.setText("Test comment");
    }

    @Test
    void createShouldReturnCreatedItem() throws Exception {
        initialize();
        when(itemService.create(anyLong(), any(NewItemDto.class))).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(newItemDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));

        verify(itemService).create(eq(1L), argThat(dto ->
                dto.getName().equals(newItemDto.getName()) &&
                        dto.getDescription().equals(newItemDto.getDescription()) &&
                        dto.getAvailable() == newItemDto.getAvailable()
        ));
    }

    @Test
    void updateShouldReturnUpdatedItem() throws Exception {
        initialize();
        when(itemService.update(anyLong(), eq(42L), any(UpdateItemDto.class))).thenReturn(itemDto);

        mvc.perform(patch("/items/42")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));

        verify(itemService).update(eq(2L), eq(42L), argThat(dto ->
                dto.getName().equals(updateItemDto.getName()) &&
                        dto.getDescription().equals(updateItemDto.getDescription()) &&
                        dto.getAvailable() == updateItemDto.getAvailable()
        ));
    }

    @Test
    void findShouldReturnFullItem() throws Exception {
        initialize();
        when(itemService.find(anyLong(), anyLong())).thenReturn(itemFullDto);

        mvc.perform(get("/items/10")
                        .header("X-Sharer-User-Id", 3L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemFullDto)));

        verify(itemService).find(eq(3L), eq(10L));
    }

    @Test
    void findAllShouldReturnListOfItems() throws Exception {
        initialize();

        when(itemService.findAll(anyLong())).thenReturn(List.of(itemFullDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemFullDto.getId()));

        verify(itemService).findAll(eq(4L));
    }

    @Test
    void searchShouldReturnMatchingItems() throws Exception {
        initialize();
        when(itemService.search(anyLong(), anyString())).thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 5L)
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()));

        verify(itemService).search(eq(5L), eq("test"));
    }

    @Test
    void addCommentShouldReturnComment() throws Exception {
        initialize();

        when(itemService.addComment(anyLong(), anyLong(), any(NewCommentDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/102/comment")
                        .header("X-Sharer-User-Id", 6L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(newCommentDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));

        verify(itemService).addComment(
                eq(6L),
                eq(102L),
                argThat(dto -> dto.getText() != null && !dto.getText().isEmpty())
        );
    }
}