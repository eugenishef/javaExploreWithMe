package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.mapper.CompilationMapper;
import ru.practicum.event.service.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PubCompilations {

    final CompilationService compilationService;

    public PubCompilations(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        List<Compilation> compilations = compilationService.getCompilations(pinned, from, size);
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable Long compilationId) {
        Compilation compilation = compilationService.getCompilationById(compilationId);
        return CompilationMapper.toDto(compilation);
    }
}