package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE "
            + "(:users IS NULL OR e.user.id IN :users) AND "
            + "(:states IS NULL OR e.status IN :states) AND "
            + "(:categories IS NULL OR e.category.id IN :categories) AND "
            + "(:start IS NULL OR e.eventDate >= :start) AND "
            + "(:end IS NULL OR e.eventDate <= :end)")
    Page<Event> findAll(
            @Param("users") List<Long> users,
            @Param("states") List<String> states,
            @Param("categories") List<Long> categories,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    Page<Event> findByUserId(Long userId, Pageable pageable);
}
