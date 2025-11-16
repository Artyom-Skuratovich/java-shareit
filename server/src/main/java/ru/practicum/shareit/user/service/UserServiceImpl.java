package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.SameEmailException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto create(UserDto dto) {
        String email = dto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new SameEmailException(String.format("Пользователь с электронной почтой '%s' уже существует", email));
        }
        return UserMapper.mapToDto(userRepository.save(UserMapper.mapToUser(dto)));
    }

    @Transactional
    @Override
    public UserDto update(long userId, UpdateUserDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        user = userRepository.save(UserMapper.updateUserProperties(user, dto));
        return UserMapper.mapToDto(user);
    }

    @Override
    public UserDto find(long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id='%d' не найден", userId))
        );
        return UserMapper.mapToDto(user);
    }

    @Transactional
    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}