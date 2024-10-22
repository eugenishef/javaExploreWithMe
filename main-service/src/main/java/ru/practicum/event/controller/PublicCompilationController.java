package ru.practicum.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.Compilation;
import ru.practicum.event.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;

    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public ResponseEntity<List<Compilation>> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        List<Compilation> compilations = compilationService.getCompilations(pinned, from, size);
        return ResponseEntity.ok(compilations);
    }

    @GetMapping("/{compilationId}")
    public ResponseEntity<Compilation> getCompilationById(@PathVariable Long compilationId) {
        Compilation compilation = compilationService.getCompilationById(compilationId);
        if (compilation != null) {
            return ResponseEntity.ok(compilation);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

