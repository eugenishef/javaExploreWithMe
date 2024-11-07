package ru.practicum.request.service;

import ru.practicum.dto.event.request.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService {
    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getAllOwnParticipationRequests(Long userId);

    ParticipationRequestDto cancelOwnParticipationInEvent(Long userId, Long requestId);
}
