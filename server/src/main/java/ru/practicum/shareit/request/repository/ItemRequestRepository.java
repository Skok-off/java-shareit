package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    default ItemRequest findRequestById(Integer id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id: " + id + " не найден"));
    }

    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Integer requestorId);

    List<ItemRequest> findAllByRequestorIdNot(Integer requestorId);
}
