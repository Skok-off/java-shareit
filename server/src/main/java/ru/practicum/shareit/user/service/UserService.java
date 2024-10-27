package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Integer id);

    UserDto getUserDto(Integer id);

    void delete(Integer id);
}
