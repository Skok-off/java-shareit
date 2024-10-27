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
import ru.practicum.shareit.config.AppHeaders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestBody @Valid ItemDto itemDto,
                          @RequestHeader(AppHeaders.USER_ID) Integer owner) {
        return itemService.create(itemDto, owner);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody @Valid ItemDto itemDto,
                          @PathVariable Integer itemId,
                          @RequestHeader(AppHeaders.USER_ID) Integer owner) {
        return itemService.update(itemDto, itemId, owner);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItem(@PathVariable Integer itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDtoOut> getAllItemsFromUser(@RequestHeader(AppHeaders.USER_ID) Integer owner) {
        return itemService.getAllItemsFromUser(owner);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Integer itemId,
                       @RequestHeader(AppHeaders.USER_ID) Integer owner) {
        itemService.delete(itemId, owner);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Integer itemId,
                                 @RequestHeader(AppHeaders.USER_ID) Integer userId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(itemId, commentDto, userId);
    }
}
