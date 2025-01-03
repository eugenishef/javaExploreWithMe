package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.request.ParticipationRequestDto;
import ru.practicum.event.service.EventPrivateService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventPrivateController {

    public static final String EVENT_ID_PATH = "/{event-id}";
    public static final String EVENT_REQUESTS_PATH = "/{event-id}/requests";

    final EventPrivateService eventPrivateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createNewEvent(@PathVariable @Positive Long userId,
                                       @RequestBody @Valid NewEventDto newEventDto) {
        return eventPrivateService.createNewEvent(userId, newEventDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllUserEvents(
            @PathVariable @Positive Long userId,
            @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive Integer size) {
        return eventPrivateService.getAllUserEvents(userId, from, size);
    }

    @GetMapping(EVENT_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable @Positive Long userId,
                                     @PathVariable("event-id") @Positive Long eventId) {
        return eventPrivateService.getEventById(userId, eventId);
    }

    @PatchMapping(EVENT_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByUser(@PathVariable @Positive Long userId,
                                          @PathVariable("event-id") @Positive Long eventId,
                                          @RequestBody  @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventPrivateService.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping(EVENT_REQUESTS_PATH)
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getParticipationRequestsOnEvent(@PathVariable @Positive Long userId,
                                                                         @PathVariable("event-id") @Positive Long eventId) {
        return eventPrivateService.getParticipationRequestsOnEvent(userId, eventId);
    }

    @PatchMapping(EVENT_REQUESTS_PATH)
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult responseOnParticipationRequests(
            @PathVariable @Positive Long userId,
            @PathVariable("event-id") @Positive Long eventId,
            @RequestBody  @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return eventPrivateService.responseOnParticipationRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }
}