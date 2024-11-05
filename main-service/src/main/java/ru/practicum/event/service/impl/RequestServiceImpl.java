package ru.practicum.event.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.event.exception.ForbiddenException;
import ru.practicum.event.exception.InvalidStatusException;
import ru.practicum.event.exception.UpdateException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Request;
import ru.practicum.event.model.RequestStatus;
import ru.practicum.event.model.User;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.RequestRepository;
import ru.practicum.event.repository.UserRepository;
import ru.practicum.event.service.RequestService;

import java.time.LocalDateTime;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Request createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UpdateException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new UpdateException("Событие не найдено"));

        Request request = new Request();
        request.setUser (user);
        request.setEvent(event);
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());

        return requestRepository.save(request);
    }

    @Override
    public Request updateRequestStatus(Long userId, Long requestId, String newStatus) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new UpdateException("Запрос не найден"));

        if (!request.getUser ().getId().equals(userId)) {
            throw new ForbiddenException("У вас нет прав для изменения этого запроса");
        }

        try {
            RequestStatus status = RequestStatus.valueOf(newStatus.toUpperCase());
            request.setStatus(status);
            return requestRepository.save(request);
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException("Некорректный статус запроса: " + newStatus);
        }
    }
}
