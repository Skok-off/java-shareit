package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoRequestOut;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Integer id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
    private List<ItemDtoRequestOut> items;
}
