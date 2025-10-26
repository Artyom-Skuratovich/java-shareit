package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(UserDto dto);

    UserDto update(long userId, UpdateUserDto dto);

    UserDto find(long userId);

    void delete(long userId);
}