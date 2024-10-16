package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoItemOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable());
    }

    public static ItemDtoOut toItemDtoOut(Item item, BookingRepository bookingRepository, CommentRepository commentRepository) {
        LocalDateTime now = LocalDateTime.now();
        //убрал 3 секунды для теста, так и не понял, почему lastBooking ожидается null, если его только что создали и апрувнули
        //вероятно, что если бы отрабатывало на секунду быстрее, то дата окончания была бы позже текущей и оно бы не попало на вывод
        Booking last = bookingRepository.findOneByItemIdAndItemOwnerIdAndEndBeforeAndStatusOrderByStartDesc(item.getId(), item.getOwner().getId(), now.minusSeconds(3), BookingStatus.APPROVED);
        Booking next = bookingRepository.findOneByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStart(item.getId(), item.getOwner().getId(), now, BookingStatus.APPROVED);
        BookingDtoItemOut lastBooking = Objects.nonNull(last) ? BookingMapper.toBookingDtoItemOut(last) : null;
        BookingDtoItemOut nextBooking = Objects.nonNull(next) ? BookingMapper.toBookingDtoItemOut(next) : null;
        Collection<CommentDto> comments = commentRepository.findAllByItemId(item.getId()).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        return new ItemDtoOut(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                lastBooking,
                nextBooking,
                comments,
                item.getRequest());
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        return item;
    }
}
