package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validation.UserValidation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemValidation itemValidation;
    private final UserValidation userValidation;
    private final EntityManager entityManager;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Integer ownerId) {
        itemValidation.forCreate(itemDto, ownerId);
        User owner = userRepository.findUserById(ownerId);
        Item item = itemMapper.toEntity(itemDto);
        item.setOwner(owner);
        item = itemRepository.save(item);
        log.info("{}{}", "Создана вещь ", item);
        return itemMapper.toDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Integer id, Integer owner) {
        itemValidation.forUpdate(id, owner);
        itemRepository.updateItem(id,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
        Item item = itemRepository.findItemById(id);
        entityManager.refresh(item);
        log.info("{}{}", "Обновлена вещь ", item);
        return itemMapper.toDto(item);
    }

    @Override
    @Transactional
    public ItemDtoOut findById(Integer id) {
        itemValidation.existsItem(id);
        return itemMapper.toItemDtoOut(itemRepository.findItemById(id),
                bookingRepository,
                commentRepository,
                bookingMapper,
                commentMapper);
    }

    @Override
    @Transactional
    public Collection<ItemDto> getAllItemsFromUser(Integer ownerId) {
        userValidation.existsUser(ownerId);
        Collection<Item> itemSet = itemRepository.findAllByOwnerId(ownerId);
        return itemSet.stream().map(itemMapper::toDto).toList();
    }

    @Override
    public Collection<ItemDto> search(String text) {
        return text.isBlank() ? new ArrayList<>() : itemRepository.search(text).stream().map(itemMapper::toDto).toList();
    }

    @Override
    public void delete(Integer id, Integer owner) {
        itemValidation.forDelete(id, owner);
        itemRepository.deleteById(id);
        log.info("{}{}", "Удалена вещь с id = ", id);
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer itemId, CommentDto commentDto, Integer authorId) {
        itemValidation.forComment(itemId, authorId);
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(userRepository.findUserById(authorId));
        comment.setItem(itemRepository.findItemById(itemId));
        comment = commentRepository.save(comment);
        log.info("{}{}", "Добавлен комментарий ", comment);
        return commentMapper.toDto(comment);
    }
}
