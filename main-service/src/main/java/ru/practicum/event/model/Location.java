package ru.practicum.event.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private double lat;
    private double lon;
}
