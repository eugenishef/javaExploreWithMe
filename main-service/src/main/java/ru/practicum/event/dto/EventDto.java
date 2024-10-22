package ru.practicum.event.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String stateAction;
    private String title;
}

