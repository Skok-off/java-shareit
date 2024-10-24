package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.config.AppHeaders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto add(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        return requestService.add(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByRequestor(@RequestHeader(AppHeaders.USER_ID) Integer userId) {
        return requestService.findAllByRequestor(userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDto findById(@RequestHeader(AppHeaders.USER_ID) Integer userId, @PathVariable Integer id) {
        return requestService.findById(id);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                        @RequestParam(name = "from") Integer from,
                                        @RequestParam(name = "size") Integer size) {
        return requestService.findAll(userId, from, size);
    }
}