package ru.practicum.shareit.user.repository;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    default User findUserById(Integer id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
    }

    boolean existsByEmail(@Email String email);

    boolean existsByEmailAndIdNot(@Email String email, Integer id);

    @Modifying
    @Query(value = """
            UPDATE users
            SET name = COALESCE(:name, name),
                email = COALESCE(:email, email)
            WHERE id = :id;
            """, nativeQuery = true)
    void updateUser(@Param("id") Integer id,
                    @Param("name") String name,
                    @Param("email") String email);
}
