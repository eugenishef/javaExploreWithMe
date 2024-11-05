package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDto {
    Long id;

    @NotBlank(message = "Название события не может быть пустым")
    String title;

    @NotBlank(message = "Описание события не может быть пустым")
    String description;

    @NotNull(message = "Дата события не может быть пустой")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    String annotation;

    Long categoryId;

    Long initiatorId;

    Boolean paid;

    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    Integer participantLimit;

    Boolean requestModeration;

    Location location;
}
