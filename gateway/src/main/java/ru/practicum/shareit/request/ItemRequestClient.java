package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

@Component
public class ItemRequestClient extends BaseClient {

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(serverUrl, builder);
    }

    public ResponseEntity<Object> create(long userId, NewItemRequestDto itemRequest) {
        return post("", userId, itemRequest);
    }

    public ResponseEntity<Object> findAllByUser(long userId) {
        return get("", userId, null);
    }

    public ResponseEntity<Object> findRequestsOfOthers(long excludedUserId) {
        return get("/all", excludedUserId, null);
    }

    public ResponseEntity<Object> find(long userId, long requestId) {
        return get(String.format("/%d", requestId), userId, null);
    }

    @Override
    protected String getApiPrefix() {
        return "/requests";
    }
}