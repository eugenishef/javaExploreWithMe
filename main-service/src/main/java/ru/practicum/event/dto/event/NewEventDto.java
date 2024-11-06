package ru.practicum.event.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.dto.location.LocationDto;
import ru.practicum.event.dto.validation.date.AfterCurrentTimeBy;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    String annotation;

    @NotBlank
    @Length(min = 20, max = 7000)
    String description;

    @Positive
    long category;

    @AfterCurrentTimeBy(hours = 2)
    String eventDate;

    LocationDto location;

    boolean paid = false;

    @PositiveOrZero
    int participantLimit = 0;

    boolean requestModeration = true;

    @Length(min = 3, max = 120)
    String title;
}
