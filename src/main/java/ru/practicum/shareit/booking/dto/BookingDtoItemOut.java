package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDtoItemOut {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserDto booker;
    private BookingStatus status;
}
