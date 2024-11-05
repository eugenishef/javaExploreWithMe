package ru.practicum.event.service;

import ru.practicum.event.dto.CompilationDto;
import ru.practicum.event.model.Compilation;
import java.util.List;

public interface CompilationService {
    List<Compilation> getCompilations(Boolean pinned, int from, int size);
    Compilation createCompilation(CompilationDto compilationDto);
    Compilation getCompilationById(Long compilationId);
    Compilation updateCompilation(Long compilationId, CompilationDto compilationUpdates);
    void deleteCompilation(Long id);
}
