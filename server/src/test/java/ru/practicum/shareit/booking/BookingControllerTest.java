package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.config.AppHeaders;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

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
public class BookingControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    private BookingServiceImpl bookingService;
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingValidation bookingValidation;
    @InjectMocks
    private BookingMapperImpl bookingMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemMapperImpl itemMapper;
    @Mock
    private UserMapperImpl userMapper;

    private MockMvc mvc;

    private User owner;
    private User booker;
    private User requester;
    private ItemRequest request;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository, bookingValidation, bookingMapper);
        bookingController = new BookingController(bookingService);
        mvc = MockMvcBuilders.standaloneSetup(bookingController).setControllerAdvice(ErrorHandler.class).build();
        mapper.registerModule(new JavaTimeModule());
        owner = new User(1, "Owner", "owner@mail.com");
        booker = new User(2, "Booker", "booker@mail.com");
        item = new Item(1, "Item", "Item description", owner, true, request);
        requester = new User(2, "Requester", "requester@mail.com");
        request = new ItemRequest(1, "Request description", requester, LocalDateTime.now());
        booking = new Booking(1,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                booker,
                BookingStatus.APPROVED);
    }

    @Test
    void addNewBooking() throws Exception {
        when(itemRepository.findItemById(any())).thenReturn(item);
        when(userRepository.findUserById(any())).thenReturn(booker);
        doNothing().when(bookingValidation).forCreate(any());
        when(bookingRepository.save(any())).thenReturn(booking);
        BookingDto bookingDto = new BookingDto(null,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item.getId(),
                null,
                null);
        mvc.perform(post("/bookings").content(mapper.writeValueAsString(bookingDto))
                        .header(AppHeaders.USER_ID, booker.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
    }

    @Test
    void updateStatus() throws Exception {
        Booking updatedBooking = new Booking(1,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                booker,
                BookingStatus.REJECTED);
        when(bookingRepository.findBookingById(any())).thenReturn(booking);
        when(bookingRepository.save(any())).thenReturn(updatedBooking);
        doNothing().when(bookingValidation).forApprove(any(), any());
        mvc.perform(patch("/bookings/{id}", booking.getId()).param("approved", "false")
                        .header(AppHeaders.USER_ID, owner.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedBooking.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(updatedBooking.getStatus().name())));
    }

    @Test
    void findById() throws Exception {
        when(bookingRepository.findBookingById(any())).thenReturn(booking);
        mvc.perform(get("/bookings/{id}", booking.getId()).characterEncoding(StandardCharsets.UTF_8)
                        .header(AppHeaders.USER_ID, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
    }

    @Test
    void getUserBookings() throws Exception {
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(any())).thenReturn(List.of(booking));
        doNothing().when(bookingValidation).forList(any());
        mvc.perform(get("/bookings").characterEncoding(StandardCharsets.UTF_8)
                        .header(AppHeaders.USER_ID, booker.getId())
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().name())));
    }

    @Test
    void getBookingsByItemsOwner() throws Exception {
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(any())).thenReturn(List.of(booking));
        mvc.perform(get("/bookings/owner").characterEncoding(StandardCharsets.UTF_8)
                        .header(AppHeaders.USER_ID, owner.getId())
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().name())));
    }
}