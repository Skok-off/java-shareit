package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.config.AppHeaders;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDtoOut create(@RequestBody @Valid BookingDto bookingDto,
                                @RequestHeader(AppHeaders.USER_ID) Integer bookerId) {
        bookingDto.setBookerId(bookerId);
        return bookingService.create(bookingDto);
    }

    @PatchMapping("{bookingId}")
    public BookingDtoOut approve(@PathVariable Integer bookingId,
                                 @RequestHeader(AppHeaders.USER_ID) Integer userId,
                                 @RequestParam(name = "approved") Boolean isApproved) {
        return bookingService.approve(bookingId, userId, isApproved);
    }

    @GetMapping("{bookingId}")
    public BookingDtoOut findBookingById(@PathVariable Integer bookingId,
                                         @RequestHeader(AppHeaders.USER_ID) Integer userId) {
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDtoOut> findByBooker(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.findAllBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoOut> findByOwner(@RequestHeader(AppHeaders.USER_ID) Integer userId,
                                                 @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.findAllBookingsByOwner(userId, state);
    }
}
