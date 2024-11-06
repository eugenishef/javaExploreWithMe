package ru.practicum.event.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.exception.*;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.RequestRepository;
import ru.practicum.event.repository.UserRepository;
import ru.practicum.event.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EventRepository eventRepository, RequestRepository requestRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<User> getUsersByIdsAndPagination(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findByIdIn(ids, pageable).getContent();
        } else {
            return userRepository.findAll(pageable).getContent();
        }
    }

    @Override
    public User createUser(User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public Event createEvent(Long userId, EventRequestDto eventRequestDto) {
        if (eventRequestDto.getParticipantLimit() != null && eventRequestDto.getParticipantLimit() < 0) {
            throw new AppException("Лимит участников не может быть отрицательным.");
        }

        Category category = categoryRepository.findById(eventRequestDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория с id " + eventRequestDto.getCategory() + " не найдена"));

        Event event = new Event();
        event.setUser(userRepository.findById(userId).orElse(null));
        event.setInitiatorId(userId);
        event.setTitle(eventRequestDto.getTitle());
        event.setAnnotation(eventRequestDto.getAnnotation());
        event.setDescription(eventRequestDto.getDescription());
        event.setEventDate(eventRequestDto.getEventDate());
        event.setCategory(category);
        event.setPaid(eventRequestDto.isPaid());
        event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        event.setRequestModeration(eventRequestDto.isRequestModeration());
        event.setLocation(new Location(eventRequestDto.getLatitude(), eventRequestDto.getLongitude()));
        event.setCreatedOn(LocalDateTime.now());
        event.setStatus(EventStatus.PENDING);

        return eventRepository.save(event);
    }


    @Override
    public Event updateEventStatus(Long userId, Long eventId, String newStatus) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (!event.getInitiatorId().equals(userId)) {
            throw new ForbiddenException("У вас нет прав для изменения статуса этого события");
        }

        try {
            EventStatus status = EventStatus.valueOf(newStatus.toUpperCase());
            event.setStatus(status);
            return eventRepository.save(event);
        } catch (IllegalArgumentException e) {
            throw new ConflictException("Некорректный статус события: " + newStatus);
        }
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
    }

    @Override
    public Event publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        event.setStatus(EventStatus.PUBLISHED);
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long eventId, EventDto eventUpdates) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id " + eventId + " не найдено"));

        if (eventUpdates.getTitle() != null) {
            existingEvent.setTitle(eventUpdates.getTitle());
        }

        if (eventUpdates.getDescription() != null) {
            existingEvent.setDescription(eventUpdates.getDescription());
        }

        if (eventUpdates.getAnnotation() != null) {
            existingEvent.setAnnotation(eventUpdates.getAnnotation());
        }

        if (eventUpdates.getEventDate() != null) {
            existingEvent.setEventDate(eventUpdates.getEventDate());
        }

        if (eventUpdates.getCategoryId() != null) {
            Category category = categoryRepository.findById(eventUpdates.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категория с id " + eventUpdates.getCategoryId() + " не найдена"));
            existingEvent.setCategory(category);
        }

        if (eventUpdates.getLocation() != null) {
            existingEvent.setLocation(parseLocation(eventUpdates.getLocation().toString()));
        }

        if (eventUpdates.getPaid() != null) {
            existingEvent.setPaid(eventUpdates.getPaid());
        }

        if (eventUpdates.getParticipantLimit() != null) {
            existingEvent.setParticipantLimit(eventUpdates.getParticipantLimit());
        }

        if (eventUpdates.getRequestModeration() != null) {
            existingEvent.setRequestModeration(eventUpdates.getRequestModeration());
        }

        return eventRepository.save(existingEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие не найдено");
        }
        eventRepository.deleteById(eventId);
    }

    @Override
    public Page<EventDto> getEvents(List<Long> users,
                                    List<String> states,
                                    List<Long> categories,
                                    String rangeStart,
                                    String rangeEnd,
                                    int from,
                                    int size) {

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart.replace(" ", "T")) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd.replace(" ", "T")) : null;

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> eventsPage = eventRepository.findAll(users, states, categories, start, end, pageable);

        return eventsPage.map(this::convertToDto);
    }

    @Override
    public List<Event> getPublicEvents(String text,
                                       List<Long> categories,
                                       Boolean paid,
                                       LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd,
                                       Boolean onlyAvailable,
                                       String sort,
                                       int from, int size) {
        return eventRepository.findAll(PageRequest.of(from / size, size)).getContent();
    }

    @Override
    public List<Event> getUserEvents(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size); //        Page<Event> eventPage = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size));
        return eventRepository.findByUserId(userId, pageable).getContent(); //        return eventPage.getContent();
    }

    @Override
    public List<Request> getEventRequests(Long userId, Long eventId) {
        return requestRepository.findByEventId(eventId);
    }

    @Override
    public Request createRequest(Long userId, Long eventId) {
        if (eventId == null) {
            throw new UpdateException("Параметр eventId обязателен");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new UpdateException("Событие не найдено"));

        List<Request> existingRequests = requestRepository.findByUserId(userId);
        if (existingRequests.stream().anyMatch(request -> request.getEvent().getId().equals(eventId))) {
            throw new ConflictException("Запрос на это событие уже существует");
        }

        Request request = new Request();
        request.setUser(new User());
        request.setEvent(event);
        request.setStatus(RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    @Override
    public void updateEventRequests(Long userId, Long eventId, RequestStatusUpdate requestStatusUpdate) {
        // Реализуйте логику для обновления статусов запросов
    }

    @Override
    public List<Request> getUserRequests(Long userId, Long eventId) {
        if (eventId == null) {
            throw new UpdateException("Не указан eventId для запроса");
        }
        return requestRepository.findByUserIdAndEventId(userId, eventId);
    }

    @Override
    public void cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        if (!request.getUser().getId().equals(userId)) {
            throw new ForbiddenException("У вас нет прав для отмены этого запроса");
        }
        requestRepository.delete(request);
    }

    private Location parseLocation(String locationString) {
        String[] parts = locationString.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Неверный формат местоположения. Ожидается 'lat,lon'.");
        }

        Double lat = Double.parseDouble(parts[0].trim());
        Double lon = Double.parseDouble(parts[1].trim());

        return new Location(lat, lon);
    }

    private EventDto convertToDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getAnnotation(),
                event.getCategory() != null ? event.getCategory().getId() : null,
                event.getUser() != null ? event.getUser().getId() : null,
                event.isPaid(),
                event.getParticipantLimit(),
                event.isRequestModeration(),
                event.getLocation()
        );
    }
}
