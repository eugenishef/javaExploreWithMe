package ru.practicum.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.event.enums.StateActionAdmin;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.validation.date.AfterCurrentTimeBy;
import ru.practicum.dto.validation.enums.EnumValidator;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    @Length(min = 20, max = 2000)
    String annotation;

    @Positive
    Long categoryId;

    @Length(min = 20, max = 7000)
    String description;

    @AfterCurrentTimeBy(hours = 2)
    String eventDate;

    LocationDto location;
    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration;
    @EnumValidator(enumClazz = StateActionAdmin.class)

    String stateAction;

    @Length(min = 3, max = 120)
    String title;
}
