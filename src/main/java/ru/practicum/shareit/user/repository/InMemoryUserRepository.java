package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.SameEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Optional;

@Deprecated
@Component
public class InMemoryUserRepository {
    private static long id = 1;
    private final HashMap<Long, User> userMap;

    public InMemoryUserRepository() {
        this.userMap = new HashMap<>();
    }

    public Optional<User> find(long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    public User create(User user) {
        String email = user.getEmail();
        if (findUserWithSameEmail(email).isPresent()) {
            throw new SameEmailException(String.format("Пользователь с электронной почтой '%s' уже существует", email));
        }
        user.setId(nextId());
        userMap.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        String email = user.getEmail();
        Optional<User> optional = findUserWithSameEmail(email);
        if (optional.isPresent() && optional.get().getId() != user.getId()) {
            throw new SameEmailException(String.format("Пользователь с электронной почтой '%s' уже существует", email));
        }
        userMap.put(user.getId(), user);
        return user;
    }

    public void delete(long userId) {
        userMap.remove(userId);
    }

    private static synchronized long nextId() {
        return id++;
    }

    private Optional<User> findUserWithSameEmail(String email) {
        return userMap.values().stream().filter(u -> u.getEmail().equals(email)).findAny();
    }
}