package ru.practicum.event.service;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface EventAdminService {
    List<EventFullDto> getAllEventsWithFilter(List<Long> usersIds,
                                              List<String> states,
                                              List<Long> categoriesIds,
                                              String rangeStartString,
                                              String rangeEndString,
                                              Integer from,
                                              Integer size
    );

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
