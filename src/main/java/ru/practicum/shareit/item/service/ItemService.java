package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Integer owner);

    ItemDto update(ItemDto itemDto, Integer id, Integer owner);

    ItemDto getItemDto(Integer id);

    Collection<ItemDto> getAllItemsFromUser(Integer owner);

    Collection<ItemDto> search(String text);

    void delete(Integer id, Integer owner);
}
