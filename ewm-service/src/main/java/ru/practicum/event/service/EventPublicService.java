package ru.practicum.event.service;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getPublicisedEventsWithFilter(String text,
                                                      List<Long> categoriesIds,
                                                      Boolean paid,
                                                      String rangeStartString,
                                                      String rangeEndString,
                                                      Boolean onlyAvailable,
                                                      String sort,
                                                      Integer from,
                                                      Integer size);

    EventFullDto getEventById(Long eventId, boolean uniqueRequest);
}
