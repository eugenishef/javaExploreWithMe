package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEventId(Long eventId);
    List<Request> findByUserId(Long userId);
    List<Request> findByUserIdAndEventId(Long userId, Long eventId);
}
