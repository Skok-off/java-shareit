package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.InMemoryUsers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final InMemoryUsers inMemoryUsers;
    private final UserValidation userValidation;

    @Override
    public UserDto create(UserDto userDto) {
        userValidation.forCreate(userDto);
        User user = UserMapper.toUser(userDto);
        userDto = UserMapper.toUserDto(inMemoryUsers.create(user));
        return userDto;
    }

    @Override
    public UserDto update(UserDto userDto, Integer id) {
        userValidation.forUpdate(userDto, id);
        User user = inMemoryUsers.getUser(id);
        if (Objects.nonNull(userDto.getName())) user.setName(userDto.getName());
        if (Objects.nonNull(userDto.getEmail())) user.setEmail(userDto.getEmail());
        userDto = UserMapper.toUserDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserDto(Integer id) {
        userValidation.existsUser(id);
        return UserMapper.toUserDto(inMemoryUsers.getUser(id));
    }

    @Override
    public void delete(Integer id) {
        userValidation.existsUser(id);
        inMemoryUsers.delete(id);
    }
}
