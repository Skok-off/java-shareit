package ru.practicum.shareit.booking.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.AccessException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidateException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BookingValidation {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public void forCreate(BookingDto bookingDto) {
        if (Objects.isNull(bookingDto.getEnd()) || bookingDto.getEnd().isBefore(LocalDateTime.now()))
            throw new ValidateException("Некорректно указана дата окончания брони.");
        if (!userRepository.existsById(bookingDto.getBookerId()))
            throw new AccessException("Пользователь с id = " + bookingDto.getBookerId() + " не может создать бронь, так как не создан сам.");
        if (!itemRepository.existsById(bookingDto.getItemId()))
            throw new NotFoundException("Нельзя создать бронь на вещь с id = " + bookingDto.getItemId() + " - её нет.");
        if (!itemRepository.existsByIdAndIsAvailableTrue(bookingDto.getItemId()))
            throw new ValidateException("Указанная вешь недоступна для бронирования.");
    }

    public void forApprove(Integer id, Integer userId) {
        if (!bookingRepository.existsById(id))
            throw new NotFoundException("Бронь с id = " + id + " не найдена.");
        if (!bookingRepository.existsByIdAndItemOwnerId(id, userId))
            throw new AccessException("Пользователь не является владельцем бронируемой вещи.");
    }

    public void forFind(Booking booking, Integer userId) {
        boolean isNotBooker = !booking.getBooker().getId().equals(userId);
        boolean isNotOwner = !booking.getItem().getOwner().getId().equals(userId);
        if (isNotBooker && isNotOwner)
            throw new AccessException("Пользователь не является автором бронирования или владельцем вещи.");
    }

    public void forList(Integer userId) {
        if (!userRepository.existsById(userId))
            throw new AccessException("Пользователь с id = " + userId + " не может получить список броней, так как его самого нет.");
    }
}
