package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {
    private final UserService userService;
    private final EntityManager entityManager;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceTest(UserService userService, EntityManager entityManager, UserMapper userMapper) {
        this.userService = userService;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
    }

    @Test
    void addNewUser() {
        UserDto userDto = new UserDto(null, "Тест Тестович Ексепшинс", "IvanIvan@email.com");
        UserDto savedUserDto = userService.create(userDto);
        assertThat(userDto.getName(), equalTo(savedUserDto.getName()));
        assertThat(userDto.getEmail(), equalTo(savedUserDto.getEmail()));
        User user = queryUserFromDb(savedUserDto.getId());
        assertThat(user.getId(), equalTo(savedUserDto.getId()));
        assertThat(user.getName(), equalTo(savedUserDto.getName()));
        assertThat(user.getEmail(), equalTo(savedUserDto.getEmail()));
    }

    @Test
    @Transactional
    void updateUser() {
        UserDto userDto = new UserDto(null, "Тест Тестович Эксепшинс", "test@email.com");
        User entity = userMapper.toEntity(userDto);
        entityManager.persist(entity);
        entityManager.flush();
        UserDto updateUserRequestDto = new UserDto(entity.getId(), "Обновлен", "up@mail.com");
        userService.update(updateUserRequestDto, entity.getId());
        entityManager.clear();
        User updatedUser = queryUserFromDb(entity.getId());
        assertThat(updatedUser.getName(), equalTo(updateUserRequestDto.getName()));
        assertThat(updatedUser.getEmail(), equalTo(updateUserRequestDto.getEmail()));
    }

    @Test
    void findById() {
        UserDto userDto = new UserDto(null, "Тест Тестович Эксепшинс", "IvanIvan@email.com");
        User entity = userMapper.toEntity(userDto);
        entityManager.persist(entity);
        entityManager.flush();
        UserDto foundDto = userService.getUserDto(entity.getId());
        assertThat(foundDto.getName(), equalTo(userDto.getName()));
        assertThat(foundDto.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void deleteById() {
        UserDto userDto = new UserDto(null, "Тест Тестович Эксепшинс", "IvanIvan@email.com");
        User entity = userMapper.toEntity(userDto);
        entityManager.persist(entity);
        entityManager.flush();
        userService.delete(entity.getId());
        assertThatThrownBy(() -> queryUserFromDb(entity.getId())).isInstanceOf(NoResultException.class);
    }

    private User queryUserFromDb(Integer id) {
        TypedQuery<User> query = entityManager.createQuery("Select u from User u where u.id = :id", User.class);
        return query.setParameter("id", id).getSingleResult();
    }
}