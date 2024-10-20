package ru.practicum.shareit.user.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.exception.ConflictException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidateException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;

    public void forCreate(UserDto userDto) {
        if (Objects.isNull(userDto.getEmail()))
            throw new ValidateException("Не указан email.");
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new ConflictException("Пользователь с таким email уже существует.");

    }

    public void forUpdate(UserDto userDto, Integer id) {
        if (userRepository.existsByEmailAndIdNot(userDto.getEmail(), id))
            throw new ConflictException("Этот email принадлежит другому пользователю.");
    }

    public void existsUser(Integer id) {
        if (!userRepository.existsById(id)) throw new NotFoundException("Пользователь не найден.");
    }
}
