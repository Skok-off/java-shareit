package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
@Transactional
public class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final EntityManager entityManager;

    Item item;
    User owner;
    User requester;
    ItemRequest request;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner Иванов", "some@email.com");
        entityManager.persist(owner);
        requester = new User(null, "Requester Петров", "someone@email.com");
        entityManager.persist(requester);
        request = new ItemRequest(null, "Description 1", requester, LocalDateTime.now());
        entityManager.persist(request);
        item = new Item(null, "Item", "Description", owner, true, request);
        entityManager.persist(item);
        entityManager.flush();
    }

    @Test
    void addNewItemRequest() {
        ItemRequestDto newRequestDto = new ItemRequestDto(null, "Request 1", null, null, null);
        ItemRequestDto savedRequestDto = itemRequestService.add(requester.getId(), newRequestDto);
        assertThat(savedRequestDto.getId(), notNullValue());
        assertThat(savedRequestDto.getItems().size(), equalTo(0));
    }

    @Test
    void findAllUserItemRequests() {
        List<ItemRequestDto> foundDto = itemRequestService.findAllByRequestor(requester.getId());
        assertThat(foundDto.size(), equalTo(1));
        assertThat(foundDto.getFirst().getRequestor().getName(), equalTo(requester.getName()));
    }

    @Test
    void findItemRequestById() {
        ItemRequestDto foundDto = itemRequestService.findById(request.getId());
        assertThat(foundDto.getId(), equalTo(request.getId()));
        assertThat(foundDto.getRequestor().getName(), equalTo(requester.getName()));
    }

    @Test
    void findAllItemRequestsFromOtherUsers() {
        List<ItemRequestDto> foundDto = itemRequestService.findAll(owner.getId(), 0, 10);
        assertThat(foundDto.size(), equalTo(1));
        assertThat(foundDto.getFirst().getRequestor().getName(), equalTo(requester.getName()));
    }
}