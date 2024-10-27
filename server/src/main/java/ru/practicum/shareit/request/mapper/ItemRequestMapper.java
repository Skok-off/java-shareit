package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDtoRequestOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface ItemRequestMapper {
    ItemRequest toEntity(ItemRequestDto itemDto);

    @Mapping(source = "requestor.id", target = "requestor.id")
    @Mapping(target = "items", expression = "java(mapToItems())")
    ItemRequestDto toDto(ItemRequest itemRequest);

    default List<ItemDtoRequestOut> mapToItems() {
        return new ArrayList<>();
    }
}
