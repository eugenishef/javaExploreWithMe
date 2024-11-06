package ru.practicum.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.StatsClient;
import ru.practicum.category.TestObjectsCategory;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.event.service.EventPrivateService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventPrivateController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventPrivateControllerTest {
    @MockBean
    private final EventPrivateService eventPrivateService;
    @MockBean
    private final StatsClient statsClient;

    private final ObjectMapper mapper;
    private final MockMvc mvc;

    private NewEventDto newEventDto;
    private EventFullDto eventFullDto;
    private EventShortDto eventShortDto;
    private UpdateEventUserRequest updateEventUserRequest;


    @BeforeEach
    void setUp() {
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);

        newEventDto = testObjectsEvent.newEventDto;
        eventFullDto = testObjectsEvent.eventFullDto;
        eventShortDto = testObjectsEvent.eventShortDto;
        updateEventUserRequest = testObjectsEvent.updateEventUserRequest;
    }

    @Test
    void createCorrectNewEvent() throws Exception {
        Mockito.when(eventPrivateService.createNewEvent(Mockito.anyLong(), Mockito.any()))
                .thenReturn(eventFullDto);

        mvc.perform(post("/users/{userId}/events", 1L)
                        .content(mapper.writeValueAsString(newEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.location.lat", is(eventFullDto.getLocation().getLat()), Float.class))
                .andExpect(jsonPath("$.eventDate", is(eventFullDto.getEventDate())));
    }

    @Test
    void createTooShortAnnotationNewEventGetBadRequest() throws Exception {
        newEventDto.setAnnotation("Short");

        mvc.perform(post("/users/{userId}/events", 1L)
                        .content(mapper.writeValueAsString(newEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void createNegativeCategoryIdNewEventGetBadRequest() throws Exception {
        newEventDto.setCategory(-1);

        mvc.perform(post("/users/{userId}/events", 1L)
                        .content(mapper.writeValueAsString(newEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void getAllUserEvents() throws Exception {
        Mockito.when(eventPrivateService.getAllUserEvents(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(eventShortDto));

        mvc.perform(get("/users/{userId}/events", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$[0].annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$[0].eventDate", is(eventFullDto.getEventDate())));
    }

    @Test
    void getEventById() throws Exception {
        Mockito.when(eventPrivateService.getEventById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(eventFullDto);

        mvc.perform(get("/users/{userId}/events/{eventId}", 1L, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.eventDate", is(eventFullDto.getEventDate())));
    }

    @Test
    void updateCorrectEventByUser() throws Exception {
        Mockito.when(eventPrivateService.updateEventByUser(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(eventFullDto);

        mvc.perform(patch("/users/{userId}/events/{eventId}", 1L, 1L)
                        .content(mapper.writeValueAsString(updateEventUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.eventDate", is(eventFullDto.getEventDate())));
    }

    @Test
    void updateCorrectEventByUserWithOnlyDate() throws Exception {
        Mockito.when(eventPrivateService.updateEventByUser(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(eventFullDto);

        UpdateEventUserRequest updateEventUserRequestOnlyDate = new UpdateEventUserRequest();
        updateEventUserRequestOnlyDate.setEventDate(updateEventUserRequest.getEventDate());

        System.out.println(updateEventUserRequestOnlyDate);

        mvc.perform(patch("/users/{userId}/events/{eventId}", 1L, 1L)
                        .content(mapper.writeValueAsString(updateEventUserRequestOnlyDate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())));
    }
}