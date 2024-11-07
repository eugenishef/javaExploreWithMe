package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.enums.EventState;

import java.time.Instant;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "confirmed_requests")
    int confirmedRequests;

    @Column(name = "created_on")
    Instant createdOn;

    String description;
    @Column(name = "event_date")

    Instant eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;

    boolean paid;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "published_on")
    Instant publishedOn;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Enumerated(EnumType.STRING)
    EventState state;

    String title;
    long views;
}
