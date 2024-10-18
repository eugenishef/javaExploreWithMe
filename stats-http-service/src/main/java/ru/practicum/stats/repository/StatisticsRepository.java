package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.stats.model.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    List<Statistics> findByRequestTimeBetween(LocalDateTime start, LocalDateTime end);
}
