package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.config.AppHeaders;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByItemsOwner(@RequestHeader(AppHeaders.USER_ID) Integer ownerId,
                                                          @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get bookings by item's owner with state {}, userId={}", stateParam, ownerId);
        return bookingClient.getBookingsByItemsOwner(ownerId, state);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                                  @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get user's bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        if (!(requestDto.getStart().isBefore(requestDto.getEnd()))) {
            throw new IllegalArgumentException("Задан неправильный период бронирования");
        }
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                             @PathVariable Integer bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                               @PathVariable Integer bookingId,
                                               @RequestParam Boolean approved) {
        log.info("Update booking {}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.updateStatus(userId, bookingId, approved);
    }
}