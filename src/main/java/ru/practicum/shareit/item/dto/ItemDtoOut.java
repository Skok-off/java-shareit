package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoItemOut;

import java.util.Collection;

@Data
@Builder
public class ItemDtoOut {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoItemOut lastBooking;
    private BookingDtoItemOut nextBooking;
    private Collection<CommentDto> comments;
    private Integer request;
}
