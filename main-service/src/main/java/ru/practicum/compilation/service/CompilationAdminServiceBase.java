package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.util.mapper.CompilationMapper;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationAdminServiceBase implements CompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.newCompilationDtoToCompilation(newCompilationDto);
        List<Long> eventsIds = newCompilationDto.getEvents();
        if (eventsIds != null && !eventsIds.isEmpty()) {
            List<Event> events = eventRepository.findAllById(eventsIds);
            compilation.setEvents(events);
        } else {
            compilation.setEvents(new ArrayList<>());
        }
        compilation = compilationRepository.save(compilation);
        return compilationMapper.compilationToCompilationDto(compilation);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = findCompilationByIdOrThrowNotFoundException(compId);
        compilationMapper.updateCompilationWithUpdateCompilationRequest(updateCompilationRequest, compilation);
        if (updateCompilationRequest.getEvents() != null) {
            List<Long> eventsIds = updateCompilationRequest.getEvents();
            List<Event> events = eventRepository.findAllById(eventsIds);
            compilation.setEvents(events);
        }
        compilation = compilationRepository.save(compilation);
        return compilationMapper.compilationToCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        findCompilationByIdOrThrowNotFoundException(compId);
        compilationRepository.deleteById(compId);
    }

    private Compilation findCompilationByIdOrThrowNotFoundException(Long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(compilationId, Compilation.class));
    }
}
