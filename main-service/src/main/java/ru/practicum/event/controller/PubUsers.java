package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PubUsers {

    final UserService userService;

    public PubUsers(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/events")
    public List<EventDto> getUserEvents(@PathVariable Long userId,
                                        @RequestParam(required = false) Integer from,
                                        @RequestParam(required = false) Integer size) {
        int pageFrom = (from != null) ? from : 0;
        int pageSize = (size != null) ? size : 10;

        List<Event> events = userService.getUserEvents(userId, pageFrom, pageSize);
        return events.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventDto> createUserEvent(@PathVariable Long userId,
                                                    @RequestBody @Valid EventRequestDto eventRequestDto) {
        Event createdEvent = userService.createEvent(userId, eventRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdEvent));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDto getUserEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        Event event = userService.getEventById(eventId);
        return convertToDto(event);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto updateUserEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody EventDto eventUpdates) {
        Event updatedEvent = userService.updateEvent(eventId, eventUpdates);
        return convertToDto(updatedEvent);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<Request> getEventRequests(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        return userService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public void updateEventRequests(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody RequestStatusUpdate requestStatusUpdate) {
        userService.updateEventRequests(userId, eventId, requestStatusUpdate);
    }

    @GetMapping("/{userId}/requests")
    public List<Request> getUserRequests(@PathVariable Long userId,
                                         @RequestParam(required = true) Long eventId) {
        return userService.getUserRequests(userId, eventId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public Request createRequest(@PathVariable Long userId,
                                 @RequestParam Long eventId) {
        return userService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public void cancelRequest(@PathVariable Long userId,
                              @PathVariable Long requestId) {
        userService.cancelRequest(userId, requestId);
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
