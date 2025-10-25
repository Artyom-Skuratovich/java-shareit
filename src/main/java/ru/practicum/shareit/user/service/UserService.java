package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    User create(UserDto dto);

    User update(long userId, UpdateUserDto dto);

    User find(long userId);

    void delete(long userId);
}