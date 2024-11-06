package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.App;

import java.util.Optional;

@Repository
public interface AppsRepository extends JpaRepository<App, Long> {
    Optional<App> findByName(String name);
}
