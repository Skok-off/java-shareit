package ru.practicum.shareit.item.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.exception.AccessException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidateException;
import ru.practicum.shareit.item.dao.InMemoryItems;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ItemValidation {
    private final InMemoryItems inMemoryItems;
    private final UserValidation userValidation;

    public void forCreate(ItemDto itemDto, Integer owner) {
        if (Objects.isNull(itemDto.getName()) || itemDto.getName().isBlank())
            throw new ValidateException("Не указано название вещи.");
        if (Objects.isNull(itemDto.getDescription()) || itemDto.getDescription().isBlank())
            throw new ValidateException("Не указано описание вещи.");
        if (Objects.isNull(itemDto.getAvailable()))
            throw new ValidateException("Не указана доступность вещи.");
        userValidation.existsUser(owner);
    }

    public void forUpdate(Integer id, Integer owner) {
        existsItem(id);
        checkOwner(id, owner);
    }

    public void forDelete(Integer id, Integer owner) {
        existsItem(id);
        checkOwner(id, owner);
    }

    public void existsItem(Integer id) {
        if (!inMemoryItems.getItems().containsKey(id)) throw new NotFoundException("Вещь не найдена.");
    }

    public void checkOwner(Integer id, Integer owner) {
        if (!Objects.equals(inMemoryItems.getItem(id).getOwner(), owner))
            throw new AccessException("Вещь принадлежит другому пользователю.");
    }
}
