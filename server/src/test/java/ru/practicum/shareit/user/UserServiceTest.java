package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.SameEmailException;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User savedUser;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");
        savedUser = userRepository.save(user);
    }

    @Test
    public void createShouldAddNewUser() {
        UserDto newUserDto = new UserDto();
        newUserDto.setName("Петр Петров");
        newUserDto.setEmail("petr@example.com");

        UserDto createdUser = userService.create(newUserDto);

        assertNotNull(createdUser);
        assertEquals("Петр Петров", createdUser.getName());
        assertEquals("petr@example.com", createdUser.getEmail());

        assertTrue(userRepository.existsById(createdUser.getId()));
    }

    @Test
    public void createShouldThrowWhenEmailExists() {
        UserDto duplicateEmailUser = new UserDto();
        duplicateEmailUser.setName("Другой Иван");
        duplicateEmailUser.setEmail("ivan@example.com");

        assertThrows(SameEmailException.class, () -> userService.create(duplicateEmailUser));
    }

    @Test
    public void findShouldReturnExistingUser() {
        UserDto foundUser = userService.find(savedUser.getId());

        assertNotNull(foundUser);
        assertEquals(savedUser.getName(), foundUser.getName());
        assertEquals(savedUser.getEmail(), foundUser.getEmail());
    }

    @Test
    public void findShouldThrowWhenUserNotExist() {
        long nonExistingId = 9999L;
        assertThrows(NotFoundException.class, () -> {
            userService.find(nonExistingId);
        });
    }

    @Test
    public void updateShouldModifyExistingUser() {
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Обновленный Иван");
        updateDto.setEmail("newivan@example.com");

        UserDto updatedUser = userService.update(savedUser.getId(), updateDto);

        assertNotNull(updatedUser);
        assertEquals("Обновленный Иван", updatedUser.getName());
        assertEquals("newivan@example.com", updatedUser.getEmail());
    }

    @Test
    public void updateShouldThrowWhenUserNotFound() {
        long nonExistingId = 9999L;
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("Нет такого");

        assertThrows(NotFoundException.class, () -> {
            userService.update(nonExistingId, dto);
        });
    }

    @Test
    public void deleteShouldRemoveUser() {
        userService.delete(savedUser.getId());
        assertFalse(userRepository.existsById(savedUser.getId()));
    }
}