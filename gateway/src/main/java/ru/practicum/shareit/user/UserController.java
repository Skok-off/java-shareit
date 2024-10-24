package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.AddUserDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        return userClient.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
        return userClient.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<Object> addNewUser(@RequestBody @Valid AddUserDto addUserRequestDto) {
        return userClient.addNewUser(addUserRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUserById(@RequestBody @Valid UpdateUserRequestDto updateUserRequestDto, @PathVariable Integer id) {
        if (updateUserRequestDto.allFieldsAreEmpty()) {
            throw new IllegalArgumentException("Должно быть задано хотя бы одно изменяемое поле");
        }
        return userClient.updateUserById(id, updateUserRequestDto);
    }
}