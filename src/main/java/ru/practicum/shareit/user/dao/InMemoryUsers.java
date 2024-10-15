package ru.practicum.shareit.user.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.HashMap;

import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_GREEN;
import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_RESET;

@Repository
@Data
@Slf4j
public class InMemoryUsers {
    private HashMap<Integer, User> users = new HashMap<>();

    public User create(User user) {
        Integer id = nextId();
        user.setId(id);
        users.put(id, user);
        log.info("{}Добавлен пользователь {}{}", ANSI_GREEN, user, ANSI_RESET);
        return getUser(id);
    }

    public User getUser(Integer id) {
        return users.getOrDefault(id, null);
    }

    public void delete(Integer id) {
        users.remove(id);
    }

    public void addItemToUser(Integer userId, Integer itemId) {
        users.get(userId).getItems().add(itemId);
    }

    public void deleteItemFromUser(Integer userId, Integer itemId) {
        users.get(userId).getItems().remove(itemId);
    }

    private Integer nextId() {
        Integer maxId = users.isEmpty() ? 0 : Collections.max(users.keySet());
        return maxId + 1;
    }
}
