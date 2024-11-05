package ru.practicum.event.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.exception.NotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<EventDto> getEvents(List<Long> users,
                                    List<String> states,
                                    List<Long> categories,
                                    String rangeStart,
                                    String rangeEnd,
                                    int from,
                                    int size) {

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart.replace(" ", "T")) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd.replace(" ", "T")) : null;

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> eventsPage = eventRepository.findAll(users, states, categories, start, end, pageable);

        return eventsPage.map(this::convertToDto);
    }

    @Override
    public List<Event> getPublicEvents(String text,
                                       List<Long> categories,
                                       Boolean paid,
                                       LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd,
                                       Boolean onlyAvailable,
                                       String sort,
                                       int from,
                                       int size) {
        return eventRepository.findAll(PageRequest.of(from / size, size)).getContent();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
    }

    private EventDto convertToDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getAnnotation(),
                event.getCategory() != null ? event.getCategory().getId() : null,
                event.getUser() != null ? event.getUser().getId() : null,
                event.isPaid(),
                event.getParticipantLimit(),
                event.isRequestModeration(),
                event.getLocation()
        );
    }
}
