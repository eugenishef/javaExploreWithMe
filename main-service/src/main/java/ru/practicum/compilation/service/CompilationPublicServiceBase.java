package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.util.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.util.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationPublicServiceBase implements CompilationPublicService {
    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> getFilteredCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilationPage;
        if (pinned != null) {
            compilationPage = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilationPage = compilationRepository.findAll(pageable).getContent();
        }
        return compilationPage.stream()
                .map(compilationMapper::compilationToCompilationDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(compId, Compilation.class));
        return compilationMapper.compilationToCompilationDto(compilation);
    }
}
