package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.SimpleUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static User updateUserProperties(User user, UpdateUserDto dto) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        return user;
    }

    public static User mapToUser(UserDto dto) {
        final User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static UserDto mapToDto(User user) {
        final UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static SimpleUserDto mapToSimpleDto(User user) {
        SimpleUserDto dto = new SimpleUserDto();
        dto.setId(user.getId());
        return dto;
    }
}