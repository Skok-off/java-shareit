package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.user.dto.AddUserDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> findById(Integer id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> deleteById(Integer id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> addNewUser(AddUserDto addUserRequestDto) {
        return post("", addUserRequestDto);
    }

    public ResponseEntity<Object> updateUserById(Integer userId, UpdateUserRequestDto updateUserRequestDto) {
        return patch("/" + userId, updateUserRequestDto);
    }
}