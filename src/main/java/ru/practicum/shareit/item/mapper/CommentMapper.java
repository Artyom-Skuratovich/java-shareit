package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public final class CommentMapper {
    private CommentMapper() {
    }

    public static Comment mapToComment(NewCommentDto dto, User author, Item item) {
        final Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static CommentDto mapToDto(Comment comment) {
        final CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}