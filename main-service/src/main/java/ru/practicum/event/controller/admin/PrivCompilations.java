package ru.practicum.event.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.CompilationDto;
import ru.practicum.event.mapper.CompilationMapper;
import ru.practicum.event.model.Compilation;
import ru.practicum.event.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
public class PrivCompilations {

    @Autowired
    private CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody CompilationDto compilationDto) {
        Compilation createdCompilation = compilationService.createCompilation(compilationDto);
        return CompilationMapper.toDto(createdCompilation);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@PathVariable Long compilationId,
                                            @Valid @RequestBody CompilationDto compilationUpdates) {
        Compilation updatedCompilation = compilationService.updateCompilation(compilationId, compilationUpdates);
        return CompilationMapper.toDto(updatedCompilation);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }
}
