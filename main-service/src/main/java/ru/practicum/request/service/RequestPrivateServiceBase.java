package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.request.ParticipationRequestDto;
import ru.practicum.util.mapper.ParticipationRequestMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.ParticipationRequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.exception.NotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RequestPrivateServiceBase implements RequestPrivateService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        User user = findUserByIdOrThrowNotFoundException(userId);
        Event event = findEventByIdOrThrowNotFoundException(eventId);

        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConflictException("initiator can non make request on own event");
        } else if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("participation in not publicised event is forbidden");
        } else if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Participation limit on event reached");
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(user);
        participationRequest.setEvent(event);
        ParticipationRequestStatus status = (!event.isRequestModeration() || event.getParticipantLimit() == 0) ?
                ParticipationRequestStatus.CONFIRMED : ParticipationRequestStatus.PENDING;
        participationRequest.setStatus(status);
        participationRequest.setCreated(Instant.now());

        participationRequest = requestRepository.save(participationRequest);
        if (status == ParticipationRequestStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return participationRequestMapper.participationRequestToParticipationRequestDto(participationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getAllOwnParticipationRequests(Long userId) {
        findUserByIdOrThrowNotFoundException(userId);
        List<ParticipationRequest> participationRequests = requestRepository.findAllByRequesterId(userId);
        return participationRequests.stream()
                .map(participationRequestMapper::participationRequestToParticipationRequestDto)
                .toList();
    }

    @Override
    public ParticipationRequestDto cancelOwnParticipationInEvent(Long userId, Long requestId) {
        findUserByIdOrThrowNotFoundException(userId);
        ParticipationRequest participationRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(requestId, ParticipationRequest.class));
        if (!Objects.equals(userId, participationRequest.getRequester().getId())) {
            throw new ConflictException("Only initiator can cancel own participation request");
        }
        participationRequest.setStatus(ParticipationRequestStatus.CANCELED);
        participationRequest = requestRepository.save(participationRequest);
        return participationRequestMapper.participationRequestToParticipationRequestDto(participationRequest);
    }

    private User findUserByIdOrThrowNotFoundException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
    }

    private Event findEventByIdOrThrowNotFoundException(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId, Event.class));
    }
}
