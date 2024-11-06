package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class NewCompilationDto {
    @Length(min = 1, max = 50)
    @NotBlank
    private String title;
    private List<@Positive Long> events;
    private boolean pinned = false;
}
