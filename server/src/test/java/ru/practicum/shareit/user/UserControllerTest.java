package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.validation.UserValidation;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidation userValidation;
    @Mock
    private UserMapperImpl userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private UserController userController;
    private MockMvc mvc;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository,
                userValidation,
                userMapper);
        userController = new UserController(userService);
        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(ErrorHandler.class).build();
        userDto = new UserDto(1, "Тест Тестович Эксепшинс", "test@mail.com");
        user = new User(1, "Тест Тестович Эксепшинс", "test@mail.com");
    }

    @Test
    void addNewUser() throws Exception {
        doNothing().when(userValidation).forCreate(any());
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toDto(any())).thenReturn(userDto);
        mvc.perform(post("/users").content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void updateUserById() throws Exception {
        UserDto updateUserRequestDto = new UserDto(1, "Обновленный", "newmail@mail.com");
        User updatedUser = new User(1, updateUserRequestDto.getName(), updateUserRequestDto.getEmail());
        doNothing().when(userValidation).forUpdate(any(), any());
        when(userRepository.findUserById(1)).thenReturn(updatedUser);
        doNothing().when(userRepository).updateUser(any(), any(), any());
        when(userMapper.toDto(any())).thenReturn(updateUserRequestDto);
        mvc.perform(patch("/users/{id}", 1).content(mapper.writeValueAsString(updateUserRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedUser.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }

    @Test
    void updateUserByWrongId() throws Exception {
        UserDto updateUserRequestDto = new UserDto(null, "Обновленный", "newmail@mail.com");
        when(userRepository.findUserById(-1)).thenThrow(new NotFoundException("ъуъ"));
        mvc.perform(patch("/users/{id}", -1).content(mapper.writeValueAsString(updateUserRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    void findById() throws Exception {
        doNothing().when(userValidation).existsUser(any());
        when(userRepository.findUserById(1)).thenReturn(user);
        when(userMapper.toDto(any())).thenReturn(userDto);
        mvc.perform(get("/users/{id}", 1).characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void findByWrongId() throws Exception {
        when(userRepository.findUserById(-1)).thenThrow(new NotFoundException("нетъ"));
        mvc.perform(get("/users/{id}", -1).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/users/{id}", 1).characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}