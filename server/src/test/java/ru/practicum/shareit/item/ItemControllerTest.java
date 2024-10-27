package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.config.AppHeaders;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.CommentMapperImpl;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.request.mapper.ItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validation.UserValidation;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private ItemValidation itemValidation;
    @Mock
    private UserValidation userValidation;
    @Mock
    private EntityManager entityManager;
    @Mock
    private ItemMapperImpl itemMapper;
    @Mock
    private CommentMapperImpl commentMapper;
    @Mock
    private BookingMapperImpl bookingMapper;
    @Mock
    private ItemRequestMapperImpl itemRequestMapper;
    @InjectMocks
    private ItemServiceImpl itemService;
    @InjectMocks
    private ItemController itemController;
    private MockMvc mvc;

    private User owner;
    private User requester;
    private User booker;
    private Item item;
    private ItemRequest request;
    private Booking lastBooking;
    private Booking nextBooking;
    private Comment comment;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository,
                userRepository,
                commentRepository,
                bookingRepository,
                itemValidation,
                userValidation,
                entityManager,
                itemMapper,
                commentMapper,
                bookingMapper,
                itemRequestMapper);
        itemController = new ItemController(itemService);
        mvc = MockMvcBuilders.standaloneSetup(itemController).setControllerAdvice(ErrorHandler.class).build();
        owner = new User(1, "Owner", "owner@mail.com");
        requester = new User(2, "Requester", "requester@mail.com");
        booker = new User(3, "Booker", "booker@mail.com");
        request = new ItemRequest(1, "Request description", requester, LocalDateTime.now());
        item = new Item(1, "Item", "Item description", owner, true, request);
        lastBooking = new Booking(1,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                booker,
                BookingStatus.APPROVED);
        nextBooking = new Booking(2,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                BookingStatus.APPROVED);
        comment = new Comment(1, "Comment", item, booker, LocalDateTime.now());
    }

    @Test
    void addNewItem() throws Exception {
        doNothing().when(itemValidation).forCreate(any(), any());
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null);
        when(itemMapper.toEntity(any())).thenReturn(item);
        when(itemMapper.toDto(any())).thenReturn(itemDto);
        when(userRepository.findUserById(any())).thenReturn(owner);
        when(itemRepository.save(any())).thenReturn(item);
        mvc.perform(post("/items").content(mapper.writeValueAsString(itemDto))
                        .header(AppHeaders.USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(item.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())));
    }

    @Test
    void findById() throws Exception {
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getText(), null, null, null);
        ItemDtoOut itemDtoOut = new ItemDtoOut(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                null, null, List.of(commentDto), null);
        when(itemMapper.toItemDtoOut(any(), any(), any(), any(), any(), any())).thenReturn(itemDtoOut);
        when(itemRepository.findItemById(1)).thenReturn(item);
        mvc.perform(get("/items/{id}", 1).characterEncoding(StandardCharsets.UTF_8)
                        .header(AppHeaders.USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())));
    }

    @Test
    void search() throws Exception {
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null);
        when(itemMapper.toDto(any())).thenReturn(itemDto);
        when(itemRepository.search(any())).thenReturn(List.of(item));
        mvc.perform(get("/items/search").param("text", "Item")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(AppHeaders.USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())));
    }

    @Test
    void updateItemById() throws Exception {
        ItemDto itemDto = new ItemDto(1, "Updated Item", "Updated description", true, null);
        Item updatedItem = new Item(1,
                itemDto.getName(),
                itemDto.getDescription(),
                owner,
                itemDto.getAvailable(),
                request);
        doNothing().when(itemValidation).forUpdate(any(), any());
        when(itemMapper.toDto(any())).thenReturn(itemDto);
        when(itemRepository.findItemById(any())).thenReturn(updatedItem);
        doNothing().when(itemRepository).updateItem(any(), any(), any(), any());
        mvc.perform(patch("/items/{id}", 1).content(mapper.writeValueAsString(itemDto))
                        .header(AppHeaders.USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedItem.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(updatedItem.getName())))
                .andExpect(jsonPath("$.description", is(updatedItem.getDescription())));
    }
}