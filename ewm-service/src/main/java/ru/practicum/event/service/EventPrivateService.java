package ru.practicum.event.service;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.request.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {
    EventFullDto createNewEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequestsOnEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult responseOnParticipationRequests(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
