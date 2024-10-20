package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Integer id;
    private String text;
    private String itemName;
    private String authorName;
    private LocalDateTime created;
}
