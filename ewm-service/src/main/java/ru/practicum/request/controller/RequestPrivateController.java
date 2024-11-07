package ru.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.request.ParticipationRequestDto;
import ru.practicum.request.service.RequestPrivateService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPrivateController {

    public static final String REQUEST_ID_PATH = "/{requestId}/cancel";

    final RequestPrivateService requestPrivateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable @Positive Long userId,
                                                              @RequestParam @Positive Long eventId) {
        return requestPrivateService.createParticipationRequest(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAllOwnParticipationRequests(@PathVariable @Positive Long userId) {
        return requestPrivateService.getAllOwnParticipationRequests(userId);
    }

    @PatchMapping(REQUEST_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelOwnParticipationInEvent(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long requestId) {
        return requestPrivateService.cancelOwnParticipationInEvent(userId, requestId);
    }
}