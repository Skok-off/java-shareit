package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
@Transactional
public class ItemServiceTest {
    private final EntityManager entityManager;
    private final ItemService itemService;
    Item item;
    User owner;
    User requester;
    User booker;
    ItemRequest request;
    Booking booking;

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
        booker = new User(null, "Booker Сидоров", "someoneelse@email.com");
        entityManager.persist(booker);
        booking = new Booking(null, LocalDateTime.now().minusDays(1), LocalDateTime.now(), item, booker, BookingStatus.APPROVED);
        entityManager.persist(booking);
        entityManager.flush();
    }

    @Test
    void addNewItem() {
        ItemDto itemDto = new ItemDto(null, "Item1", "Description 1", true, request.getId());
        ItemDto savedDto = itemService.create(itemDto, owner.getId());
        assertThat(savedDto.getId(), notNullValue());
        assertThat(savedDto.getName(), equalTo(itemDto.getName()));
        assertThat(savedDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(savedDto.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void updateItemById() {
        ItemDto itemDto = new ItemDto(null, "Item1", "Description 1", true, request.getId());
        ItemDto savedDto = itemService.create(itemDto, owner.getId());
        ItemDto updateItemRqDto = new ItemDto(owner.getId(), "Updated Item1", "Updated Description 1", false, null);
        ItemDto updatedItem = itemService.update(updateItemRqDto, savedDto.getId(), owner.getId());
        assertThat(updatedItem.getId(), notNullValue());
        assertThat(updatedItem.getName(), equalTo(updateItemRqDto.getName()));
        assertThat(updatedItem.getDescription(), equalTo(updateItemRqDto.getDescription()));
        assertThat(updatedItem.getAvailable(), equalTo(updateItemRqDto.getAvailable()));
    }

    @Test
    void findById() {
        ItemDto itemDto = new ItemDto(null, "Item1", "Description 1", true, request.getId());
        ItemDto savedDto = itemService.create(itemDto, owner.getId());
        ItemDtoOut foundDto = itemService.findById(savedDto.getId());
        assertThat(foundDto.getId(), notNullValue());
        assertThat(foundDto.getName(), equalTo(itemDto.getName()));
        assertThat(foundDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(foundDto.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void addNewComment() {
        CommentDto commentDto = new CommentDto(null, "Comment 1", null, null, null);
        CommentDto savedCommentDto = itemService.addComment(item.getId(),
                commentDto,
                booker.getId());
        assertThat(savedCommentDto.getId(), notNullValue());
        assertThat(savedCommentDto.getAuthorName(), equalTo(booker.getName()));
        assertThat(savedCommentDto.getText(), equalTo(commentDto.getText()));
    }
}