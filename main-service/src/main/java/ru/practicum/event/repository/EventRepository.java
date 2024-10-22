package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Category;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategory(Category category, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "(:users IS NULL OR e.user.id IN :users) AND " +
            "(:states IS NULL OR e.status IN :states) AND " +
            "(:categories IS NULL OR e.category.id IN :categories) AND " +
            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findByFilters(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    Optional<Event> findByUserIdAndId(Long userId, Long eventId);

    List<Event> findByUserId(Long userId);

    Optional<Event> findByIdAndUserId(Long eventId, Long userId);
}

