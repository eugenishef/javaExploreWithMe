package ru.practicum.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.CategoryRepository;
import ru.practicum.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(@RequestParam(required = false) List<Long> users,
                                                 @RequestParam(required = false) List<String> states,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) String rangeStart,
                                                 @RequestParam(required = false) String rangeEnd,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        List<Event> events = eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{eventId}/publish")
    public ResponseEntity<Event> publishEvent(@PathVariable Long eventId) {
        Event publishedEvent = eventService.publishEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).body(publishedEvent);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody EventDto eventDto) {
        Event event = eventService.findEventById(eventId);
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        event.setCategory(category);
        return ResponseEntity.ok(eventService.saveEvent(event));
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }
}
