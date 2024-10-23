package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    default Booking findBookingById(Integer id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Бронь с id: " + id + " не найдена"));
    }

    boolean existsByIdAndItemOwnerId(Integer id, Integer owner);

    boolean existsByItemIdAndBookerIdAndStatusAndEndBefore(Integer itemId, Integer bookerId, BookingStatus status, LocalDateTime now);

    Booking findOneByItemIdAndItemOwnerIdAndEndBeforeAndStatusOrderByStartDesc(Integer itemId, Integer itemOwnerId, LocalDateTime now, BookingStatus status);

    Booking findOneByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStart(Integer itemId, Integer itemOwnerId, LocalDateTime now, BookingStatus status);

    // Поиски по пользователю, разместившему бронь
    Collection<Booking> findAllByBookerIdOrderByStartDesc(Integer userId);

    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer userId, LocalDateTime now, LocalDateTime nowAgain);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Integer userId, LocalDateTime currentDate);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Integer userId, LocalDateTime currentDate);

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Integer userId, BookingStatus status);

    // Поиски по владельцу вещи
    Collection<Booking> findAllByItemOwnerIdOrderByStartDesc(Integer owner);

    Collection<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer owner, LocalDateTime currentDate, LocalDateTime currentDateAgain);

    Collection<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Integer owner, LocalDateTime currentDate);

    Collection<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Integer owner, LocalDateTime currentDate);

    Collection<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Integer owner, BookingStatus status);
}
