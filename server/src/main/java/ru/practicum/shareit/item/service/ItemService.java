package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Integer owner);

    ItemDto update(ItemDto itemDto, Integer id, Integer owner);

    ItemDtoOut findById(Integer id);

    List<ItemDtoOut> getAllItemsFromUser(Integer owner);

    Collection<ItemDto> search(String text);

    void delete(Integer id, Integer owner);

    CommentDto addComment(Integer itemId, CommentDto commentDto, Integer userId);
}
