package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.config.AppHeaders;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.AddCommentRequestDto;
import ru.practicum.shareit.item.dto.AddItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestHeader(AppHeaders.USER_ID) Integer userId, @RequestBody @Valid AddItemRequestDto newItemDto) {
        return itemClient.addNewItem(userId, newItemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findItemById(@RequestHeader(AppHeaders.USER_ID) Integer userId, @PathVariable Integer id) {
        return itemClient.findItemById(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByOwner(@RequestHeader(AppHeaders.USER_ID) Integer userId) {
        return itemClient.findAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                             @RequestParam(defaultValue = "") String text) {
        return itemClient.searchItem(userId, text);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                             @PathVariable("id") Integer itemId,
                                             @RequestBody @Valid UpdateItemRequestDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                             @PathVariable("id") Integer itemId,
                                             @RequestBody @Valid AddCommentRequestDto commentDto) {
        return itemClient.addNewComment(userId, itemId, commentDto);
    }
}