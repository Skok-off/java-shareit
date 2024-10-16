package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.UserValidation;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidation userValidation;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        userValidation.forCreate(userDto);
        User user = UserMapper.toUser(userDto);
        userDto = UserMapper.toUserDto(userRepository.save(user));
        log.info("{}{}", "Создан пользователь ", user);
        return userDto;
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Integer id) {
        userValidation.forUpdate(userDto, id);
        userRepository.updateUser(id, userDto.getName(), userDto.getEmail());
        User user = userRepository.findUserById(id);
        userDto = UserMapper.toUserDto(user);
        log.info("{}{}", "Обновлен пользователь ", user);
        return userDto;
    }

    @Override
    public UserDto getUserDto(Integer id) {
        userValidation.existsUser(id);
        return UserMapper.toUserDto(userRepository.findUserById(id));
    }

    @Override
    public void delete(Integer id) {
        userValidation.existsUser(id);
        userRepository.deleteById(id);
        log.info("{}{}", "Удален пользователь с id = ", id);
    }
}
