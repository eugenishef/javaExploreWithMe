package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class NewUserRequest {
    @Email
    @NotNull
    @Length(min = 6, max = 254)
    private String email;
    @Length(min = 2, max = 250)
    @NotBlank
    private String name;
}
