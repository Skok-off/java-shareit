package ru.practicum.shareit.item.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.InMemoryUsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_GREEN;
import static ru.practicum.shareit.helper.ColorsForConsole.ANSI_RESET;

@Repository
@Data
@Slf4j
public class InMemoryItems {
    private final InMemoryUsers inMemoryUsers;
    private HashMap<Integer, Item> items = new HashMap<>();

    public Item create(Item item) {
        Integer id = nextId();
        item.setId(id);
        items.put(id, item);
        inMemoryUsers.addItemToUser(item.getOwner(), id);
        log.info("{}Добавлена вещь {}{}", ANSI_GREEN, item, ANSI_RESET);
        return getItem(id);
    }

    public Item getItem(Integer id) {
        return items.getOrDefault(id, null);
    }

    public void delete(Integer id) {
        inMemoryUsers.deleteItemFromUser(items.get(id).getOwner(), id);
        items.remove(id);
    }

    public Collection<ItemDto> search(String text) {
        String lowerCaseText = text.toLowerCase();
        Collection<ItemDto> itemsList = new ArrayList<>();
        if (!text.isBlank()) {
            itemsList = items.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> {
                        String name = item.getName().toLowerCase();
                        String description = item.getDescription().toLowerCase();
                        return name.contains(lowerCaseText) || description.contains(lowerCaseText);
                    })
                    .map(ItemMapper::toItemDto)
                    .toList();
        }
        return itemsList;
    }

    private Integer nextId() {
        Integer maxId = items.isEmpty() ? 0 : Collections.max(items.keySet());
        return maxId + 1;
    }
}
