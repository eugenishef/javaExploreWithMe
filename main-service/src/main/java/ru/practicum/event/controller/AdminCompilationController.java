package ru.practicum.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.CompilationRequest;
import ru.practicum.event.model.Compilation;
import ru.practicum.event.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public ResponseEntity<Compilation> createCompilation(@RequestBody CompilationRequest compilationRequest) {
        Compilation compilation = compilationService.createCompilation(compilationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(compilation);
    }

    @PatchMapping("/{compilationId}")
    public ResponseEntity<Compilation> updateCompilation(@PathVariable Long compilationId,
                                                         @RequestBody CompilationRequest compilationRequest) {
        Compilation updatedCompilation = compilationService.updateCompilation(compilationId, compilationRequest);
        return ResponseEntity.ok(updatedCompilation);
    }

    @DeleteMapping("/{compilationId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compilationId) {
        compilationService.deleteCompilation(compilationId);
        return ResponseEntity.noContent().build();
    }
}
