package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.item.dto.AddCommentRequestDto;
import ru.practicum.shareit.item.dto.AddItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> addNewItem(Integer userId, AddItemRequestDto newItemDto) {
        return post("", userId, newItemDto);
    }

    public ResponseEntity<Object> findItemById(Integer userId, Integer id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> findAllItemsByOwner(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItem(Integer userId, String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> updateItem(Integer userId, Integer itemId, UpdateItemRequestDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> addNewComment(Integer userId, Integer itemId, AddCommentRequestDto commentDto) {
        Map<String, Object> parameters = Map.of("itemId", itemId);
        return post("/{itemId}/comment", userId, parameters, commentDto);
    }
}