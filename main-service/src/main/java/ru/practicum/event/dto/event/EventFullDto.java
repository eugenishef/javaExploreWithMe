package ru.practicum.event.dto.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.category.CategoryDto;
import ru.practicum.event.dto.location.LocationDto;
import ru.practicum.event.dto.user.UserShortDto;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
    UserShortDto initiator;
    LocationDto location;
    boolean paid;
    int participantLimit;
    String publishedOn;
    boolean requestModeration;
    String state;
    String title;
    long views;
}
