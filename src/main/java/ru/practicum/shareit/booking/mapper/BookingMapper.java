package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItemOut;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Objects;

@RequiredArgsConstructor
public class BookingMapper {
    public static BookingDtoOut toBookingDtoOut(Booking booking) {
        return new BookingDtoOut(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus());
    }

    public static BookingDtoItemOut toBookingDtoItemOut(Booking booking) {
        return new BookingDtoItemOut(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus());
    }

    public static Booking toBooking(BookingDto bookingDto, ItemRepository itemRepository, UserRepository userRepository) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(itemRepository.findItemById(bookingDto.getItemId()));
        booking.setBooker(userRepository.findUserById(bookingDto.getBookerId()));
        booking.setStatus(Objects.nonNull(bookingDto.getStatus()) ? bookingDto.getStatus() : BookingStatus.WAITING);
        return booking;
    }
}
