package ru.practicum.event.dto.compilation;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    @Length(min = 1, max = 50)
    String title;

    List<@Positive Long> events;
    Boolean pinned;
}
