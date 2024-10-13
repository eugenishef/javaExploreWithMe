package ru.practicum.stats.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@Data
@NoArgsConstructor
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app", nullable = false)
    private String app;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "request_time", nullable = false)
    private LocalDateTime requestTime;
}
