package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

@Component
public class UserClient extends BaseClient {

    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(serverUrl, builder);
    }

    public ResponseEntity<Object> create(NewUserDto user) {
        return post("", null, user);
    }

    public ResponseEntity<Object> update(long userId, UpdateUserDto user) {
        return patch(String.format("/%d", userId), null, null, user);
    }

    public ResponseEntity<Object> find(long userId) {
        return get(String.format("/%d", userId), null, null);
    }

    public ResponseEntity<Object> delete(long userId) {
        return delete(String.format("/%d", userId));
    }

    @Override
    protected String getApiPrefix() {
        return "/users";
    }
}