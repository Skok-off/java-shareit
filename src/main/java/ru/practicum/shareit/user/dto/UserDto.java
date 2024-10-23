package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    @Email
    private String email;
}
