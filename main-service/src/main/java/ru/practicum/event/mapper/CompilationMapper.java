package ru.practicum.event.mapper;

import ru.practicum.event.dto.CompilationDto;
import ru.practicum.event.model.Compilation;

public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.isPinned(),
                compilation.getEventIds()
        );
    }
}
