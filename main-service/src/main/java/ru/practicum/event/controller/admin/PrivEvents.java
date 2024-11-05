package ru.practicum.event.controller.admin;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivEvents {

    final UserService userService;
    final EventRepository eventRepository;

    public PrivEvents(UserService userService, EventRepository eventRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam int from,
            @RequestParam int size) {

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart.replace(" ", "T")) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd.replace(" ", "T")) : null;

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Event> eventsPage = eventRepository.findAll(users, states, categories, start, end, pageable);

        List<EventDto> eventDtos = eventsPage.stream().map(this::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok(eventDtos);
    }

    @PutMapping("/{eventId}/publish")
    public Event publishEvent(@PathVariable Long eventId) {
        return userService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}")
    public Event updateEvent(@PathVariable Long eventId,
                             @Valid @RequestBody EventDto eventUpdates) {
        return userService.updateEvent(eventId, eventUpdates);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventId) {
        userService.deleteEvent(eventId);
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
