package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.util.mapper.EventMapper;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.event.enums.ParticipationRequestUpdateStatus;
import ru.practicum.dto.event.enums.StateActionUser;
import ru.practicum.dto.event.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.request.ParticipationRequestDto;
import ru.practicum.util.mapper.ParticipationRequestMapper;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.util.mapper.LocationMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventState;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.BadRequestException;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.exception.NotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.model.enums.ParticipationRequestStatus.CONFIRMED;
import static ru.practicum.model.enums.ParticipationRequestStatus.PENDING;
import static ru.practicum.model.enums.ParticipationRequestStatus.REJECTED;

@Service
@RequiredArgsConstructor
public class EventPrivateServiceBase extends EventServiceUtil implements EventPrivateService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    public EventFullDto createNewEvent(Long userId, NewEventDto newEventDto) {
        User initiator = findUserByIdOrThrowNotFoundException(userId);
        Category category = findCategoryByIdOrThrowNotFoundException(newEventDto.getCategory(), categoryRepository);
        Event event = eventMapper.newEventDtoToEvent(newEventDto);

        event.setInitiator(initiator);
        event.setCategory(category);
        Location location = event.getLocation();
        if (location != null) {
            location = locationRepository.save(location);
            event.setLocation(location);
        }
        event.setCreatedOn(Instant.now());
        event.setState(EventState.PENDING);
        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllUserEvents(Long userId, Integer from, Integer size) {
        findUserByIdOrThrowNotFoundException(userId);
        Pageable pageable = PageRequest.of(from, size);
        List<Event> eventPage = eventRepository.findAllByInitiatorId(userId, pageable);
        return eventPage.stream()
                .map(eventMapper::eventToeventShortDto)
                .toList();
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = findEventByIdOrThrowNotFoundException(eventId, eventRepository);
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        findUserByIdOrThrowNotFoundException(userId);
        Event event = findEventByIdOrThrowNotFoundException(eventId, eventRepository);
        if (event.getState() == EventState.PUBLISHED)
            throw new ConflictException("Couldn't update published event");
        eventMapper.updateEventUserRequestIgnoringLocationAndCategoryId(updateEventUserRequest, event);
        if (event.getEventDate().isBefore(Instant.now().plus(Duration.ofHours(2))))
            throw new BadRequestException("Event date must be after current time not less then by 2 hours");
        LocationDto newLocationDto = updateEventUserRequest.getLocation();
        if (newLocationDto != null) {
            updateLocation(event, locationRepository, locationMapper, newLocationDto);
        }
        Long newCategoryId = updateEventUserRequest.getCategoryId();
        if (newCategoryId != null && newCategoryId != 0) {
            Category newCategory = findCategoryByIdOrThrowNotFoundException(newCategoryId, categoryRepository);
            event.setCategory(newCategory);
        }
        if (updateEventUserRequest.getStateAction() != null) {
            StateActionUser stateActionUser = StateActionUser.valueOf(updateEventUserRequest.getStateAction());
            switch (stateActionUser) {
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
            }
        }
        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsOnEvent(Long userId, Long eventId) {
        findUserByIdOrThrowNotFoundException(userId);
        Event event = findEventByIdOrThrowNotFoundException(eventId, eventRepository);
        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConflictException(String.format("user id=%d is not initiator of event id=%d", userId, eventId));
        }
        return requestRepository.findAllByEventId(eventId).stream()
                .map(participationRequestMapper::participationRequestToParticipationRequestDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult responseOnParticipationRequests(
            Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        User user = findUserByIdOrThrowNotFoundException(userId);
        Event event = findEventByIdOrThrowNotFoundException(eventId, eventRepository);
        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConflictException(String.format("user id=%d is not initiator of event id=%d", userId, eventId));
        }
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<ParticipationRequest> requests = requestRepository.findAllById(requestIds);

        Optional<ParticipationRequest> isNotPendingStatus = requests.stream()
                .filter(request -> request.getStatus() != PENDING)
                .findFirst();
        if (isNotPendingStatus.isPresent())
            throw new ConflictException("Only pending requests could be updated");

        ParticipationRequestUpdateStatus status =
                ParticipationRequestUpdateStatus.valueOf(eventRequestStatusUpdateRequest.getStatus());

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (status == ParticipationRequestUpdateStatus.REJECTED) {
            for (ParticipationRequest request : requests) {
                request.setStatus(REJECTED);
            }

            requests = requestRepository.saveAll(requests);

            rejectedRequests = requests.stream()
                    .map(participationRequestMapper::participationRequestToParticipationRequestDto)
                    .toList();
        } else {
            int participantLimit = event.getParticipantLimit();
            int confirmedRequestsCount = event.getConfirmedRequests();

            if (participantLimit <= confirmedRequestsCount) {
                throw new ConflictException("Participation limit on event reached");
            }

            for (ParticipationRequest request : requests) {
                if (participantLimit > confirmedRequestsCount) {
                    request.setStatus(CONFIRMED);
                    confirmedRequests.add(
                            participationRequestMapper.participationRequestToParticipationRequestDto(request));
                    confirmedRequestsCount++;
                } else {
                    request.setStatus(REJECTED);
                    rejectedRequests.add(
                            participationRequestMapper.participationRequestToParticipationRequestDto(request));
                }
            }

            event.setConfirmedRequests(confirmedRequestsCount);
            eventRepository.save(event);
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }

    private User findUserByIdOrThrowNotFoundException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
    }
}
