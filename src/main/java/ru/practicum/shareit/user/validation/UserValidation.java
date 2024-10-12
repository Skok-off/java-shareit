package ru.practicum.shareit.user.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.exception.ConflictException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidateException;
import ru.practicum.shareit.user.dao.InMemoryUsers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final InMemoryUsers inMemoryUsers;

    public void forCreate(UserDto userDto) {
        if (Objects.isNull(userDto.getEmail())) throw new ValidateException("Не указан email.");
        HashMap<Integer, User> users = inMemoryUsers.getUsers();
        if (!users.isEmpty() && users.values()
                .stream()
                .anyMatch(user -> Objects.equals(user.getEmail(), userDto.getEmail())))
            throw new ConflictException("Пользователь с таким email уже существует.");

    }

    public void forUpdate(UserDto userDto, Integer id) {
        HashMap<Integer, User> users = inMemoryUsers.getUsers();
        if (!users.isEmpty() && users.values()
                .stream()
                .anyMatch(user -> !Objects.equals(user.getId(), id)
                        && Objects.equals(user.getEmail(), userDto.getEmail())))
            throw new ConflictException("Этот email принадлежит другому пользователю.");
    }

    public void existsUser(Integer id) {
        if (!inMemoryUsers.getUsers().containsKey(id)) throw new NotFoundException("Пользователь не найден.");
    }
}
