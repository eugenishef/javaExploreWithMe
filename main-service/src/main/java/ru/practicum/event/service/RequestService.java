package ru.practicum.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.RequestStatusUpdate;
import ru.practicum.event.model.Request;
import ru.practicum.event.repository.RequestRepository;

import java.util.List;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> getEventRequests(Long userId, Long eventId) {
        return requestRepository.findByEventId(eventId);
    }

    public List<Request> updateRequestStatuses(Long userId, Long eventId, RequestStatusUpdate requestStatusUpdate) {
        List<Request> requests = requestRepository.findByEventId(eventId);

        for (Request request : requests) {
            if (requestStatusUpdate.getRequestIds().contains(request.getId())) {
                request.setStatus(requestStatusUpdate.getStatus());
                requestRepository.save(request);
            }
        }

        return requests;
    }
}
