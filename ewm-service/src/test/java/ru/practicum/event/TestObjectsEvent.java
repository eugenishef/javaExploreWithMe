package ru.practicum.event;

import ru.practicum.category.TestObjectsCategory;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.event.enums.StateActionAdmin;
import ru.practicum.dto.event.enums.StateActionUser;
import ru.practicum.dto.location.LocationDto;

import java.time.Duration;
import java.time.Instant;

import static ru.practicum.config.EWMServiceAppConfig.DATE_TIME_FORMATTER;

public class TestObjectsEvent {
    public NewEventDto newEventDto;
    public EventFullDto eventFullDto;
    public EventShortDto eventShortDto;
    public LocationDto locationDto;
    public UpdateEventUserRequest updateEventUserRequest;
    public UpdateEventAdminRequest updateEventAdminRequest;

    public TestObjectsEvent(TestObjectsCategory testObjectsCategory) {
        CategoryDto categoryDto = testObjectsCategory.categoryDto;
        Instant eventDate = Instant.now().plus(Duration.ofDays(2));
        locationDto = new LocationDto();
        locationDto.setLat(30);
        locationDto.setLat(60);

        newEventDto = new NewEventDto();
        newEventDto.setTitle("Title");
        newEventDto.setEventDate(DATE_TIME_FORMATTER.format(eventDate));
        newEventDto.setAnnotation("Annotation should be length not less then 20 symbols");
        newEventDto.setPaid(true);
        newEventDto.setDescription("Description should be length not less then 20 symbols");
        newEventDto.setLocation(locationDto);
        newEventDto.setCategory(1);
        newEventDto.setParticipantLimit(50);
        newEventDto.setRequestModeration(true);

        eventFullDto = new EventFullDto();
        eventFullDto.setTitle(newEventDto.getTitle());
        eventFullDto.setEventDate(newEventDto.getEventDate());
        eventFullDto.setAnnotation(newEventDto.getAnnotation());
        eventFullDto.setPaid(newEventDto.isPaid());
        eventFullDto.setDescription(newEventDto.getDescription());
        eventFullDto.setLocation(newEventDto.getLocation());
        eventFullDto.setCategory(categoryDto);
        eventFullDto.setParticipantLimit(newEventDto.getParticipantLimit());
        eventFullDto.setRequestModeration(newEventDto.isRequestModeration());
        eventFullDto.setId(1L);

        eventShortDto = new EventShortDto();
        eventShortDto.setTitle(eventFullDto.getTitle());
        eventShortDto.setEventDate(eventFullDto.getEventDate());
        eventShortDto.setAnnotation(eventFullDto.getAnnotation());
        eventShortDto.setPaid(eventFullDto.isPaid());
        eventShortDto.setCategory(eventFullDto.getCategory());

        updateEventUserRequest = new UpdateEventUserRequest();
        updateEventUserRequest.setTitle(newEventDto.getTitle());
        updateEventUserRequest.setEventDate(newEventDto.getEventDate());
        updateEventUserRequest.setAnnotation(newEventDto.getAnnotation());
        updateEventUserRequest.setPaid(newEventDto.isPaid());
        updateEventUserRequest.setDescription(newEventDto.getDescription());
        updateEventUserRequest.setLocation(newEventDto.getLocation());
        updateEventUserRequest.setCategoryId(newEventDto.getCategory());
        updateEventUserRequest.setParticipantLimit(newEventDto.getParticipantLimit());
        updateEventUserRequest.setRequestModeration(newEventDto.isRequestModeration());
        updateEventUserRequest.setStateAction(StateActionUser.CANCEL_REVIEW.toString());

        updateEventAdminRequest = new UpdateEventAdminRequest();
        updateEventAdminRequest.setTitle(newEventDto.getTitle());
        updateEventAdminRequest.setEventDate(newEventDto.getEventDate());
        updateEventAdminRequest.setAnnotation(newEventDto.getAnnotation());
        updateEventAdminRequest.setPaid(newEventDto.isPaid());
        updateEventAdminRequest.setDescription(newEventDto.getDescription());
        updateEventAdminRequest.setLocation(newEventDto.getLocation());
        updateEventAdminRequest.setCategoryId(newEventDto.getCategory());
        updateEventAdminRequest.setParticipantLimit(newEventDto.getParticipantLimit());
        updateEventAdminRequest.setRequestModeration(newEventDto.isRequestModeration());
        updateEventAdminRequest.setStateAction(StateActionAdmin.PUBLISH_EVENT.toString());
    }
}
