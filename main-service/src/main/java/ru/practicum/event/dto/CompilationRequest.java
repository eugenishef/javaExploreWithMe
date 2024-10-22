package ru.practicum.event.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompilationRequest {
    private String title;
    private List<Long> events;
    private boolean pinned;
}

