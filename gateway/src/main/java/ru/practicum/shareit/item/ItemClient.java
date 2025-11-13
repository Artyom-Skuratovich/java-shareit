package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Map;

@Component
public class ItemClient extends BaseClient {

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(serverUrl, builder);
    }

    public ResponseEntity<Object> create(long userId, NewItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> update(long userId, long itemId, UpdateItemDto item) {
        return patch(String.format("/%d", itemId), userId, null, item);
    }

    public ResponseEntity<Object> find(long userId, long itemId) {
        return get(String.format("/%d", itemId), userId, null);
    }

    public ResponseEntity<Object> findAll(long userId) {
        return get("", userId, null);
    }

    public ResponseEntity<Object> search(long userId, String text) {
        return get("/search?text={text}", userId, Map.of("text", text));
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, NewCommentDto comment) {
        return post(String.format("/%d/comment", itemId), userId, comment);
    }

    @Override
    protected String getApiPrefix() {
        return "/items";
    }
}