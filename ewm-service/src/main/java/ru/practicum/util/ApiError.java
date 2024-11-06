package ru.practicum.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString(exclude = "errors")
public class ApiError {
    @JsonIgnore
    private List<StackTraceElement> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
