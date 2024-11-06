package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.util.mapper.EventMapper;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.enums.StateActionAdmin;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.util.mapper.LocationMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.model.enums.EventState;
import ru.practicum.util.exception.ConflictException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventAdminServiceBase extends EventServiceUtil implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    @Override
    public List<EventFullDto> getAllEventsWithFilter(List<Long> usersIds,
                                                     List<String> states,
                                                     List<Long> categoriesIds,
                                                     String rangeStartString,
                                                     String rangeEndString,
                                                     Integer from,
                                                     Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        QEvent qEvent = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (usersIds != null && !usersIds.isEmpty()) {
            conditions.add(qEvent.initiator.id.in(usersIds));
        }
        if (states != null && !states.isEmpty()) {
            List<EventState> eventStates = states.stream()
                    .map(EventState::valueOf)
                    .toList();
            conditions.add(qEvent.state.in(eventStates));
        }
        if (categoriesIds != null && !categoriesIds.isEmpty()) {
            List<Category> categoryList = categoryRepository.findAllById(categoriesIds);
            conditions.add(qEvent.category.in(categoryList));
        }
        if (rangeStartString != null && !rangeStartString.isBlank()) {
            conditions.add(eventDateAfter(rangeStartString));
        }
        if (rangeEndString != null && !rangeEndString.isBlank()) {
            conditions.add(eventDateBefore(rangeEndString));
        }

        Page<Event> result;
        if (conditions.isEmpty()) {
            result = eventRepository.findAll(pageable);
        } else {
            BooleanExpression finalCondition = conditions.stream()
                    .reduce(BooleanExpression::and)
                    .get();
            result = eventRepository.findAll(finalCondition, pageable);
        }

        return result.stream()
                .map(eventMapper::eventToEventFullDto)
                .toList();
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findEventByIdOrThrowNotFoundException(eventId, eventRepository);
        eventMapper.updateEventAdminRequestIgnoringLocationAndCategoryId(updateEventAdminRequest, event);
        LocationDto newLocationDto = updateEventAdminRequest.getLocation();
        if (newLocationDto != null) {
            updateLocation(event, locationRepository, locationMapper, newLocationDto);
        }
        Long newCategoryId = updateEventAdminRequest.getCategoryId();
        if (newCategoryId != null && newCategoryId != 0) {
            Category newCategory = findCategoryByIdOrThrowNotFoundException(newCategoryId, categoryRepository);
            event.setCategory(newCategory);
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            StateActionAdmin stateActionAdmin = StateActionAdmin.valueOf(updateEventAdminRequest.getStateAction());
            switch (stateActionAdmin) {
                case PUBLISH_EVENT -> {
                    if (event.getState() == EventState.PUBLISHED)
                        throw new ConflictException("Event already published");
                    if (event.getState() == EventState.CANCELED)
                        throw new ConflictException("Couldn't publish canceled event");
                    event.setState(EventState.PUBLISHED);
                }
                case REJECT_EVENT -> {
                    if (event.getState() == EventState.PUBLISHED)
                        throw new ConflictException("Couldn't cancel published event");
                    event.setState(EventState.CANCELED);
                }
            }
        }
        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }
}
