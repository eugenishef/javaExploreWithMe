package ru.practicum.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.event.enums.StateActionUser;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.validation.enums.EnumValidator;

@Data
public class UpdateEventUserRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    @Positive
    private Long categoryId;
    @Length(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @EnumValidator(enumClazz = StateActionUser.class)
    private String stateAction;
    @Length(min = 3, max = 120)
    private String title;
}
