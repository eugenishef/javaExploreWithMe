package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatsClient;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.validation.enums.EnumValidator;
import ru.practicum.event.enums.EventsSort;
import ru.practicum.event.service.EventPublicService;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static ru.practicum.config.EWMServiceAppConfig.APP_NAME;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final EventPublicService eventPublicService;
    private final StatsClient statsClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getPublicisedEventsWithFilter(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<@Positive Long> categoriesIds,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStartString,
            @RequestParam(name = "rangeEnd", required = false) String rangeEndString,
            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) @EnumValidator(enumClazz = EventsSort.class) String sort,
            @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        statsClient.createRecord(new EndpointHitDto(APP_NAME, request.getRequestURI(), request.getRemoteAddr()));
        return eventPublicService.getPublicisedEventsWithFilter(
                text, categoriesIds, paid, rangeStartString, rangeEndString, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable @Positive Long eventId,
                                     HttpServletRequest request) {
        String uri = request.getRequestURI();
        long hitsBefore = getEndpointUniqueHits(uri);
        statsClient.createRecord(new EndpointHitDto(APP_NAME, uri, request.getRemoteAddr()));
        long hitsAfter = getEndpointUniqueHits(uri);
        boolean uniqueRequest = hitsAfter > hitsBefore;
        return eventPublicService.getEventById(eventId, uniqueRequest);
    }

    private long getEndpointUniqueHits(String uri) {
        long hits = 0;
        Optional<ViewStatsDto> viewStatsDtoOptional =
                statsClient.getStats(Instant.EPOCH, Instant.now(), List.of(uri), true).stream()
                        .filter(viewStatsDto -> viewStatsDto.getApp().equals(APP_NAME))
                        .findFirst();
        if (viewStatsDtoOptional.isPresent()) {
            hits = viewStatsDtoOptional.get().getHits();
        }
        return hits;
    }
}
