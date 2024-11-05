package ru.practicum.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Email
    @Size(min = 6, max = 64)
    @Column(nullable = false, unique = true)
    String email;

    @NotBlank
    @Size(min = 2, max = 250)
    @Column(nullable = false)
    String name;
}
