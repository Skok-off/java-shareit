package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDtoItemOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoRequestOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {CommentMapper.class, ItemRequestMapper.class, ItemRequest.class})
public interface ItemMapper {
    @Mapping(source = "requestId", target = "request")
    Item toEntity(ItemDto itemDto);

    ItemDto toDto(Item item);

    ItemDtoRequestOut toDtoRequest(Item item);

    default ItemRequest mapRequestIdToItemRequest(Integer requestId) {
        ItemRequest itemRequest = null;
        if (Objects.nonNull(requestId)) {
            itemRequest = new ItemRequest();
            itemRequest.setId(requestId);
        }
        return itemRequest;
    }

    default ItemDtoOut toItemDtoOut(Item item,
                                    BookingRepository bookingRepository,
                                    CommentRepository commentRepository,
                                    BookingMapper bookingMapper,
                                    CommentMapper commentMapper,
                                    ItemRequestMapper itemRequestMapper) {
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
        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(item.getRequest());
        return ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .request(itemRequestDto)
                .build();
    }
}
