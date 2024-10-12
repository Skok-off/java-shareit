package ru.practicum.shareit.user.model;

import lombok.Data;

import java.util.HashSet;

@Data
public class User {
    private final HashSet<Integer> items;
    private Integer id;
    private String name;
    private String email;

    public User() {
        this.items = new HashSet<>();
    }
}
