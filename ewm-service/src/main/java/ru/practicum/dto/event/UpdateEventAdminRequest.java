package ru.practicum.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.event.enums.StateActionAdmin;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.validation.date.AfterCurrentTimeBy;
import ru.practicum.dto.validation.enums.EnumValidator;

@Data
public class UpdateEventAdminRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    @Positive
    private Long categoryId;
    @Length(min = 20, max = 7000)
    private String description;
    @AfterCurrentTimeBy(hours = 2)
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @EnumValidator(enumClazz = StateActionAdmin.class)
    private String stateAction;
    @Length(min = 3, max = 120)
    private String title;
}
