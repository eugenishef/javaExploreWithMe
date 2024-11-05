package ru.practicum.event.service;

import org.springframework.data.domain.Page;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.*;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Page<EventDto> getEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size
    );

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

    Event getEventById(Long id);
}
