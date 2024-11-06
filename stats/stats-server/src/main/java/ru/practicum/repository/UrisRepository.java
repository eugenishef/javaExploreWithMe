package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Uri;

import java.util.Optional;

@Repository
public interface UrisRepository extends JpaRepository<Uri, Long> {
    Optional<Uri> findByName(String name);
}
