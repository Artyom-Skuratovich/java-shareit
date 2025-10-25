package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.InMemoryUserRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final InMemoryUserRepository userRepository;

    @Override
    public User create(UserDto dto) {
        return userRepository.create(UserMapper.mapToUser(dto));
    }

    @Override
    public User update(long userId, UpdateUserDto dto) {
        Optional<User> optional = userRepository.find(userId);
        if (optional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        return userRepository.update(UserMapper.updateUserProperties(optional.get(), dto));
    }

    @Override
    public User find(long userId) {
        Optional<User> optional = userRepository.find(userId);
        if (optional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id='%d' не найден", userId));
        }
        return optional.get();
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }
}