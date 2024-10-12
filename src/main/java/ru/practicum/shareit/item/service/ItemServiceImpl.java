package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.InMemoryItems;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.user.dao.InMemoryUsers;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final InMemoryItems inMemoryItems;
    private final InMemoryUsers inMemoryUsers;
    private final ItemValidation itemValidation;
    private final UserValidation userValidation;

    @Override
    public ItemDto create(ItemDto itemDto, Integer owner) {
        itemValidation.forCreate(itemDto, owner);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        return ItemMapper.toItemDto(inMemoryItems.create(item));
    }

    @Override
    public ItemDto update(ItemDto itemDto, Integer id, Integer owner) {
        itemValidation.forUpdate(id, owner);
        Item item = inMemoryItems.getItem(id);
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        Integer request = item.getRequest();
        if (Objects.nonNull(name)) item.setName(name);
        if (Objects.nonNull(description)) item.setDescription(description);
        if (Objects.nonNull(available)) item.setAvailable(available);
        if (Objects.nonNull(request)) item.setRequest(request);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemDto(Integer id) {
        itemValidation.existsItem(id);
        return ItemMapper.toItemDto(inMemoryItems.getItem(id));
    }

    @Override
    public Collection<ItemDto> getAllItemsFromUser(Integer owner) {
        userValidation.existsUser(owner);
        HashSet<Integer> itemIds = inMemoryUsers.getUser(owner).getItems();
        Collection<ItemDto> itemDtos = new HashSet<>();
        for (Integer id : itemIds) {
            itemDtos.add(getItemDto(id));
        }
        return itemDtos;
    }

    @Override
    public Collection<ItemDto> search(String text) {
        return inMemoryItems.search(text);
    }

    @Override
    public void delete(Integer id, Integer owner) {
        itemValidation.forDelete(id, owner);
        inMemoryItems.delete(id);
    }
}
