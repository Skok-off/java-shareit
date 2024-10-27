package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoItemOut;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoOut {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoItemOut lastBooking;
    private BookingDtoItemOut nextBooking;
    private Collection<CommentDto> comments;
    private ItemRequestDto request;
}
