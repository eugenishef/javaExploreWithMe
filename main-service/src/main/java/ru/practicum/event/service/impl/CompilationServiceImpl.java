package ru.practicum.event.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.CompilationDto;
import ru.practicum.event.exception.NotFoundException;
import ru.practicum.event.model.Compilation;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.CompilationRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.CompilationService;

import java.util.List;

@Service
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Autowired
    public CompilationServiceImpl(EventRepository eventRepository, CompilationRepository compilationRepository) {
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return (pinned != null)
                ? compilationRepository.findByPinned(pinned, pageable)
                : compilationRepository.findAll(pageable).getContent();
    }

    @Override
    public Compilation createCompilation(CompilationDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
//        if (events.size() != compilationDto.getEvents().size()) {
//            throw new AppException("Некоторые события не найдены.");
//        }

        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setEvents(events);
        compilation.setPinned(compilationDto.isPinned());

        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation getCompilationById(Long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + compilationId + " не найдена"));
    }

    @Override
    public Compilation updateCompilation(Long compilationId, CompilationDto compilationUpdates) {
        Compilation existingCompilation = getCompilationById(compilationId);

        if (compilationUpdates.getTitle() != null) {
            existingCompilation.setTitle(compilationUpdates.getTitle());
        }

        if (compilationUpdates.getEvents() != null && !compilationUpdates.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(compilationUpdates.getEvents());

            // Проверяем наличие всех событий
//            if (events.size() != compilationUpdates.getEvents().size()) {
//                throw new NotFoundException("Некоторые события не найдены.");
//            }

            existingCompilation.setEvents(events);
        }

        existingCompilation.setPinned(compilationUpdates.isPinned());

        return compilationRepository.save(existingCompilation);
    }

    @Override
    public void deleteCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Подборка с id " + id + " не найдена");
        }
        compilationRepository.deleteById(id);
    }
}
