package ru.practicum.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.CompilationRequest;
import ru.practicum.event.model.Compilation;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.CompilationRepository;
import ru.practicum.event.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompilationService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CompilationRepository compilationRepository;

    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);  // Пагинация

        if (pinned != null) {
            return compilationRepository.findByPinned(pinned, pageable);
        } else {
            return compilationRepository.findAll(pageable).getContent();
        }
    }

    public Compilation getCompilationById(Long compilationId) {
        return compilationRepository.findById(compilationId).orElse(null);
    }

    public Compilation createCompilation(CompilationRequest compilationRequest) {
        List<Event> events = eventRepository.findAllById(compilationRequest.getEvents());
        if (events.isEmpty()) {
            throw new IllegalArgumentException("Events not found for the given IDs");
        }

        Compilation compilation = new Compilation();
        compilation.setTitle(compilationRequest.getTitle());
        compilation.setPinned(compilationRequest.isPinned());
        compilation.setEvents(events);

        return compilationRepository.save(compilation);
    }

    public Compilation updateCompilation(Long id, CompilationRequest compilationRequest) {
        Optional<Compilation> optionalCompilation = compilationRepository.findById(id);
        if (optionalCompilation.isEmpty()) {
            throw new IllegalArgumentException("Compilation not found with id: " + id);
        }

        Compilation compilation = optionalCompilation.get();
        compilation.setTitle(compilationRequest.getTitle());
        compilation.setPinned(compilationRequest.isPinned());

        if (compilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(compilationRequest.getEvents());
            if (events.isEmpty()) {
                throw new IllegalArgumentException("Events not found for the given IDs");
            }
            compilation.setEvents(events);
        }

        return compilationRepository.save(compilation);
    }

    public void deleteCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new IllegalArgumentException("Compilation not found with id: " + id);
        }
        compilationRepository.deleteById(id);
    }

    public List<Compilation> findAll() {
        return compilationRepository.findAll();
    }
}
