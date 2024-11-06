package ru.practicum.dto.event.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.event.enums.ParticipationRequestUpdateStatus;
import ru.practicum.dto.validation.enums.EnumValidator;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    @NotNull
    List<@Positive Long> requestIds;

    @EnumValidator(enumClazz = ParticipationRequestUpdateStatus.class)
    String status;
}
