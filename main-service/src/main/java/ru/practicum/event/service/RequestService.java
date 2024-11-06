package ru.practicum.event.service;

public interface RequestService {
    Request createRequest(Long userId, Long eventId);

    Request updateRequestStatus(Long userId, Long requestId, String newStatus);
}
