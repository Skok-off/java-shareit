package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
}
