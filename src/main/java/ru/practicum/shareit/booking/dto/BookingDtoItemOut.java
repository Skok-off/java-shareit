package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoItemOut {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserDto booker;
    private BookingStatus status;
}
