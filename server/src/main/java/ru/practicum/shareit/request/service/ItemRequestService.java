package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto add(Integer userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllByRequestor(Integer userId);

    ItemRequestDto findById(Integer id);

    List<ItemRequestDto> findAll(Integer userId, Integer from, Integer size);
}
