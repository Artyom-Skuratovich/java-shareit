package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.InMemoryUserRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final InMemoryUserRepository userRepository;

    @Override
    public UserDto create(UserDto dto) {
        return UserMapper.mapToDto(userRepository.create(UserMapper.mapToUser(dto)));
    }

    @Override
    public UserDto update(long userId, UpdateUserDto dto) {
        User user = userRepository.find(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        user = userRepository.update(UserMapper.updateUserProperties(user, dto));
        return UserMapper.mapToDto(user);
    }

    @Override
    public UserDto find(long userId) {
        User user = userRepository.find(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        return UserMapper.mapToDto(user);
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }
}