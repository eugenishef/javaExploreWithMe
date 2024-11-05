package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestDto {

    @NotBlank(message = "Название события не может быть пустым")
    String title;

    String annotation;

    @NotBlank(message = "Описание события не может быть пустым")
    String description;

    @NotNull(message = "Дата события не может быть пустой")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull(message = "ID категории не может быть пустым")
    Long category;

    boolean paid;

    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    Integer participantLimit;

    boolean requestModeration;

    double latitude;
    double longitude;
}
