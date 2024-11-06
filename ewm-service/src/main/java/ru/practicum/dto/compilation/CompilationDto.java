package ru.practicum.dto.compilation;

import lombok.Data;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private String title;
    private List<EventShortDto> events;
    private boolean pinned;
}