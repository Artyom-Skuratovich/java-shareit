package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody UserDto user) {
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public User update(
            @PathVariable long userId,
            @Valid @RequestBody UpdateUserDto user) {
        return userService.update(userId, user);
    }

    @GetMapping("/{userId}")
    public User find(@PathVariable long userId) {
        return userService.find(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
    }
}