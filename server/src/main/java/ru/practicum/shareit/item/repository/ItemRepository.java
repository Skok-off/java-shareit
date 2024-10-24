package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    default Item findItemById(Integer id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + id + " не найдена"));
    }

    @Query(value = """
            SELECT *
            FROM items i
            WHERE i.is_available = true
            AND (i.name ILIKE %:text%
              OR i.description ILIKE %:text%)
            """, nativeQuery = true)
    Collection<Item> search(String text);

    @Modifying
    @Query(value = """
            UPDATE items
            SET name = COALESCE(:name, name),
                description = COALESCE(:description, description),
                is_available = COALESCE(:available, is_available)
            WHERE id = :id;
            """, nativeQuery = true)
    void updateItem(@Param("id") Integer id,
                    @Param("name") String name,
                    @Param("description") String description,
                    @Param("available") Boolean available);

    boolean existsByIdAndAvailableTrue(Integer id);

    Collection<Item> findAllByOwnerId(Integer ownerId);

    List<Item> findAllByRequestId(Integer requestId);
}
