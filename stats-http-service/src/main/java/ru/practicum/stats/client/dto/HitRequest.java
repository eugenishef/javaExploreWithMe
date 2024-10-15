package ru.practicum.stats.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HitRequest {
    @NotBlank(message = "Поле 'app' обязательно для заполнения")
    private String app;

    @NotBlank(message = "Поле 'uri' обязательно для заполнения")
    private String uri;

    @NotBlank(message = "Поле 'ip' обязательно для заполнения")
    private String ip;
}
