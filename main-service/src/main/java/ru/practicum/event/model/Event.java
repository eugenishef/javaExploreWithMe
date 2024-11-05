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

    Long initiatorId;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    EventStatus status = EventStatus.PENDING;

    @Embedded
    Location location;

    @Column(name = "created_on", nullable = false, updatable = false)
    LocalDateTime createdOn;

//    @Column(name = "lat")
//    double lat;
//
//    @Column(name = "lon")
//    double lon;

    @PrePersist
    public void onCreate() {
        this.createdOn = LocalDateTime.now();
        this.status = EventStatus.PENDING;
    }
}
