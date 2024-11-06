package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;  // Изменено с Long initiatorId на User для сохранения связи

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String annotation;

    @Column(nullable = false)
    String description;

    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(nullable = false)
    boolean paid;

    @Column(name = "participant_limit", columnDefinition = "integer default 0")
    int participantLimit;

    @Column(nullable = false)
    boolean requestModeration;

    @Enumerated(EnumType.STRING)
    EventStatus status = EventStatus.PENDING;

    @Embedded
    Location location;

    @Column(name = "created_on", nullable = false, updatable = false)
    LocalDateTime createdOn;

    @PrePersist
    public void onCreate() {
        this.createdOn = LocalDateTime.now();
        this.status = EventStatus.PENDING;
    }
}
