package ru.practicum.event.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.model.*;
import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    List<User> getUsersByIdsAndPagination(List<Long> ids, int from, int size);
    User createUser(User newUser);
    void deleteUser(Long userId);
    Event createEvent(Long userId, @Valid EventRequestDto event);
    Event updateEventStatus(Long userId, Long eventId, String newStatus);
    Event getEventById(Long id);
    Event publishEvent(Long eventId);
    Event updateEvent(Long eventId, EventDto eventUpdates);
    void deleteEvent(Long eventId);
    Page<EventDto> getEvents(List<Long> users, List<String> states, List<Long> categories,
                             String rangeStart, String rangeEnd, int from, int size);
    List<Event> getPublicEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size
    );
    List<Event> getUserEvents(Long userId, int from, int size);
    List<Request> getEventRequests(Long userId, Long eventId);
    Request createRequest(Long userId, Long eventId);
    void updateEventRequests(
            Long userId,
            Long eventId,
            RequestStatusUpdate requestStatusUpdate
    );
    List<Request> getUserRequests(Long userId, Long eventId);
    void cancelRequest(Long userId, Long requestId);
}
