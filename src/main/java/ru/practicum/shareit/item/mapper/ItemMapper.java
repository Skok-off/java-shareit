package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
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

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface ItemMapper {
    Item toEntity(ItemDto itemDto);

    ItemDto toDto(Item item);

    default ItemDtoOut toItemDtoOut(Item item,
                                    BookingRepository bookingRepository,
                                    CommentRepository commentRepository,
                                    BookingMapper bookingMapper,
                                    CommentMapper commentMapper) {
        LocalDateTime now = LocalDateTime.now();
        Booking last = bookingRepository.findOneByItemIdAndItemOwnerIdAndEndBeforeAndStatusOrderByStartDesc(
                item.getId(),
                item.getOwner().getId(),
                now.minusSeconds(3),
                BookingStatus.APPROVED);
        Booking next = bookingRepository.findOneByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStart(
                item.getId(),
                item.getOwner().getId(),
                now,
                BookingStatus.APPROVED);
        BookingDtoItemOut lastBooking = Objects.nonNull(last) ? bookingMapper.toBookingDtoItemOut(last) : null;
        BookingDtoItemOut nextBooking = Objects.nonNull(next) ? bookingMapper.toBookingDtoItemOut(next) : null;
        Collection<CommentDto> comments = commentRepository.findAllByItemId(item.getId())
                .stream()
                .map(commentMapper::toDto)
                .toList();
        return ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .request(item.getRequest())
                .build();
    }
}
