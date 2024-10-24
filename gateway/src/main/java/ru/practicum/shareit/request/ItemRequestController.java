package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.config.AppHeaders;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.AddItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addNewItemRequest(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                                    @RequestBody @Valid AddItemRequestDto newItemRequestDto) {
        return requestClient.addNewItemRequest(userId, newItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUserItemRequests(@RequestHeader(AppHeaders.USER_ID) Integer userId) {
        return requestClient.findAllUserItemRequests(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findItemRequestById(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                                      @PathVariable Integer id) {
        return requestClient.findItemRequestById(userId, id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequestsFromOtherUsers(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all item requests from other users userId={}, from={}, size={}", userId, from, size);
        return requestClient.findAllItemRequestsFromOtherUsers(userId, from, size);
    }
}