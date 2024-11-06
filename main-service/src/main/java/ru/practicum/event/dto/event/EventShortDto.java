package ru.practicum.event.dto.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.category.CategoryDto;
import ru.practicum.event.dto.user.UserShortDto;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String eventDate;
    UserShortDto initiator;
    boolean paid;
    String title;
    long views;
}
