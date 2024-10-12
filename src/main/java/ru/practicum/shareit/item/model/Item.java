package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Integer owner;
    private Boolean available;
    private Integer request;
}
