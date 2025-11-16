package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentMapperTest {

    @Test
    void mapToCommentShouldCreateCommentWithCorrectFields() {
        NewCommentDto dto = new NewCommentDto();
        dto.setText("Test comment");

        User author = new User();
        author.setId(1L);
        author.setName("AuthorName");

        Item item = new Item();
        item.setId(2L);
        item.setName("ItemName");

        LocalDateTime before = LocalDateTime.now();
        Comment comment = CommentMapper.mapToComment(dto, author, item);
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(comment);
        assertEquals("Test comment", comment.getText());
        assertEquals(author, comment.getAuthor());
        assertEquals(item, comment.getItem());

        assertTrue((comment.getCreated().isEqual(before) || comment.getCreated().isAfter(before))
                && (comment.getCreated().isEqual(after) || comment.getCreated().isBefore(after)));
    }

    @Test
    void mapToDtoShouldConvertCommentToCommentDto() {
        User author = new User();
        author.setId(5L);
        author.setName("Commenter");

        Comment comment = new Comment();
        comment.setId(100L);
        comment.setText("Some comment");
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.of(2023, 10, 15, 12, 0));

        CommentDto dto = CommentMapper.mapToDto(comment);

        assertEquals(100L, dto.getId());
        assertEquals("Some comment", dto.getText());
        assertEquals("Commenter", dto.getAuthorName());
        assertEquals(LocalDateTime.of(2023, 10, 15, 12, 0), dto.getCreated());
    }

    @Test
    void mapToDtoShouldThrowNPEWhenCommentIsNull() {
        assertThrows(NullPointerException.class, () -> {
            CommentMapper.mapToDto(null);
        });
    }
}