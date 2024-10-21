package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingValidation bookingValidation;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDtoOut create(BookingDto bookingDto) {
        bookingValidation.forCreate(bookingDto);
        User booker = userRepository.findUserById(bookingDto.getBookerId());
        Item item = itemRepository.findItemById(bookingDto.getItemId());
        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        log.info("{}{}", "Создана бронь ", booking);
        return bookingMapper.toBookingDtoOut(booking);
    }

    @Override
    @Transactional
    public BookingDtoOut approve(Integer id, Integer userId, Boolean isApproved) {
        bookingValidation.forApprove(id, userId);
        BookingStatus status = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        Booking booking = bookingRepository.findBookingById(id);
        booking.setStatus(status);
        bookingRepository.save(booking);
        log.info("{} {} {}", "Обновлен статус брони", status, booking);
        return bookingMapper.toBookingDtoOut(booking);
    }

    @Override
    @Transactional
    public BookingDtoOut findBookingById(Integer id, Integer userId) {
        Booking booking = bookingRepository.findBookingById(id);
        bookingValidation.forFind(booking, userId);
        return bookingMapper.toBookingDtoOut(bookingRepository.findBookingById(id));
    }

    @Override
    @Transactional
    public Collection<BookingDtoOut> findAllBookingsByBooker(Integer userId, BookingState state) {
        bookingValidation.forList(userId);
        Collection<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL -> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case CURRENT ->
                    bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING ->
                    bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED ->
                    bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
        }
        return bookings.stream().map(bookingMapper::toBookingDtoOut).toList();
    }

    @Override
    @Transactional
    public Collection<BookingDtoOut> findAllBookingsByOwner(Integer userId, BookingState state) {
        bookingValidation.forList(userId);
        Collection<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL -> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT ->
                    bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING ->
                    bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED ->
                    bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
        }
        return bookings.stream().map(bookingMapper::toBookingDtoOut).toList();
    }
}
