package ru.practicum.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.RequestStatusUpdate;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Request;
import ru.practicum.event.service.EventService;
import ru.practicum.event.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
public class UserEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @Autowired
    public UserEventController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        List<Event> events = eventService.getUserEvents(userId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getUserEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        Event event = eventService.getUserEventById(userId, eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@PathVariable Long userId, @RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long userId,
                                             @PathVariable Long eventId,
                                             @RequestBody Event eventUpdates) {
        Event updatedEvent = eventService.updateEvent(userId, eventId, eventUpdates);
        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<Request>> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        List<Request> requests = requestService.getEventRequests(userId, eventId);
        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<List<Request>> updateRequestStatuses(@PathVariable Long userId, @PathVariable Long eventId,
                                                               @RequestBody RequestStatusUpdate requestStatusUpdate) {
        List<Request> updatedRequests = requestService.updateRequestStatuses(userId, eventId, requestStatusUpdate);
        return ResponseEntity.ok(updatedRequests);
    }
}
