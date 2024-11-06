package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.QEvent;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.mapper.LocationMapper;

import java.time.Instant;

import static ru.practicum.config.EWMServiceAppConfig.DATE_TIME_FORMATTER;

public abstract class EventServiceUtil {
    protected void updateLocation(Event event,
                                   LocationRepository locationRepository,
                                   LocationMapper locationMapper,
                                   LocationDto newLocationDto) {
        Location location = event.getLocation();
        locationMapper.updateLocationWithLocationDto(newLocationDto, location);
        location = locationRepository.save(location);
        event.setLocation(location);
    }

    protected BooleanExpression eventDateAfter(String start) {
        Instant rangeStartInstant = DATE_TIME_FORMATTER.parse(start, Instant::from);
        return QEvent.event.eventDate.after(rangeStartInstant);
    }

    protected BooleanExpression eventDateBefore(String end) {
        Instant rangeEndInstant = DATE_TIME_FORMATTER.parse(end, Instant::from);
        return QEvent.event.eventDate.before(rangeEndInstant);
    }

    protected Category findCategoryByIdOrThrowNotFoundException(Long categoryId, CategoryRepository categoryRepository) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(categoryId, Category.class));
    }

    protected Event findEventByIdOrThrowNotFoundException(Long eventId, EventRepository eventRepository) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId, Event.class));
    }
}
