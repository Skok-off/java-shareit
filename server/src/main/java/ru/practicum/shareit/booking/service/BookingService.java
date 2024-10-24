package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingDtoOut create(BookingDto bookingDto);

    BookingDtoOut approve(Integer id, Integer userId, Boolean isApproved);

    BookingDtoOut findBookingById(Integer id, Integer userId);

    Collection<BookingDtoOut> findAllBookingsByBooker(Integer userId, BookingState state);

    Collection<BookingDtoOut> findAllBookingsByOwner(Integer userId, BookingState state);
}
