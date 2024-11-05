package ru.practicum.event.service;

import ru.practicum.event.model.Request;

public interface RequestService {
    Request createRequest(Long userId, Long eventId);
    Request updateRequestStatus(Long userId, Long requestId, String newStatus);
}
