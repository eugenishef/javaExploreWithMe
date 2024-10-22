package ru.practicum.event.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStatus;
import ru.practicum.event.repository.EventRepository;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        event.setStatus(EventStatus.PENDING);
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    public List<Event> getEventsByCategory(Long categoryId, Pageable pageable) {
        Category category = new Category();
        category.setId(categoryId);
        return eventRepository.findByCategory(category, pageable);
    }

    public Event publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id " + eventId));

        event.setStatus(EventStatus.PUBLISHED);

        return eventRepository.save(event);
    }

    public Event updateEvent(Long userId, Long eventId, Event eventUpdates) {
        Event existingEvent = eventRepository.findByUserIdAndId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        existingEvent.setAnnotation(eventUpdates.getAnnotation());
        existingEvent.setDescription(eventUpdates.getDescription());
        existingEvent.setEventDate(eventUpdates.getEventDate());
        existingEvent.setTitle(eventUpdates.getTitle());
        existingEvent.setPaid(eventUpdates.isPaid());
        existingEvent.setParticipantLimit(eventUpdates.getParticipantLimit());
        existingEvent.setRequestModeration(eventUpdates.isRequestModeration());

        return eventRepository.save(existingEvent);
    }

    public List<Event> getUserEvents(Long userId) {
        return eventRepository.findByUserId(userId);
    }

    public Event getUserEventById(Long userId, Long eventId) {
        return eventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("Event not found for user"));
    }

    public Event getEventByUserIdAndEventId(Long userId, Long eventId) {
        return eventRepository.findByUserIdAndId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Event not found for user " + userId + " and event " + eventId));
    }

    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found with id " + eventId);
        }

        eventRepository.deleteById(eventId);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEvents(List<Long> users, List<String> states, List<Long> categories,
                                 String rangeStart, String rangeEnd, int from, int size) {
        return eventRepository.findAll();
    }

    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getPublicEvents(Long userId, int from, int size) {

        return eventRepository.findAll(PageRequest.of(from / size, size)).getContent();
    }

}

