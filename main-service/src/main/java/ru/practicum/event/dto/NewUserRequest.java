package ru.practicum.event.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;
}

