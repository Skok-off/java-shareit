package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDtoRequestOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public ItemRequestDto add(Integer userId, ItemRequestDto itemRequestDto) {
        UserDto userDto = userMapper.toDto(userRepository.findUserById(userId));
        itemRequestDto.setRequestor(userDto);
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = itemRequestMapper.toEntity(itemRequestDto);
        itemRequest = requestRepository.save(itemRequest);
        log.info("{}{}", "Добавлен запрос ", itemRequest);
        entityManager.clear();
        return itemRequestMapper.toDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> findAllByRequestor(Integer userId) {
        List<ItemRequest> requests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);
        log.info("{}{}", "Запрошен список запросов от пользователя с id = ", userId);
        return requests.stream().map(itemRequestMapper::toDto).toList();
    }

    @Override
    public ItemRequestDto findById(Integer id) {
        ItemRequest request = requestRepository.findRequestById(id);
        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(request);
        List<ItemDtoRequestOut> itemDtos = itemRepository.findAllByRequestId(id).stream().map(itemMapper::toDtoRequest).toList();
        itemRequestDto.setItems(itemDtos);
        log.info("{}{}", "Найден запрос по id ", itemRequestDto);
        return itemRequestDto;
    }

    public List<ItemRequestDto> findAll(Integer userId, Integer from, Integer size) {
        List<ItemRequest> requests = requestRepository.findAllByRequestorIdNot(userId);
        log.info("Выводим все запросы");
        return requests.stream().map(itemRequestMapper::toDto).toList();
    }
}