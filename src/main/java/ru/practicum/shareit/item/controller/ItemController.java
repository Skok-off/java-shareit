package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestBody @Valid ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Integer owner) {
        return itemService.create(itemDto, owner);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody @Valid ItemDto itemDto,
                          @PathVariable Integer itemId,
                          @RequestHeader("X-Sharer-User-Id") Integer owner) {
        return itemService.update(itemDto, itemId, owner);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Integer itemId) {
        return itemService.getItemDto(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsFromUser(@RequestHeader("X-Sharer-User-Id") Integer owner) {
        return itemService.getAllItemsFromUser(owner);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Integer itemId,
                       @RequestHeader("X-Sharer-User-Id") Integer owner) {
        itemService.delete(itemId, owner);
    }
}
