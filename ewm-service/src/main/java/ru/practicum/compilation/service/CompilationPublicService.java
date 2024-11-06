package ru.practicum.compilation.service;

import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getFilteredCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
