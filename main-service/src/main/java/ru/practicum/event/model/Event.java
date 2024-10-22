package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String annotation;

    @Column(nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private boolean paid;

    @Column(name = "participant_limit", columnDefinition = "integer default 0")
    private int participantLimit;

    private boolean requestModeration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @JsonProperty("category")
    public void setCategoryId(Long categoryId) {
        this.category = new Category(categoryId);
    }
}

