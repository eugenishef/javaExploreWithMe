package ru.practicum.event.controller;

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
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.event.service.EventPublicService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventPublicController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventPublicControllerTest {
    @MockBean
    private final EventPublicService eventPublicService;
    @MockBean
    private final StatsClient statsClient;

    private final MockMvc mvc;

    private EventFullDto eventFullDto;
    private EventShortDto eventShortDto;

    @BeforeEach
    void setUp() {
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);

        eventFullDto = testObjectsEvent.eventFullDto;
        eventShortDto = testObjectsEvent.eventShortDto;
    }

    @Test
    void getPublicisedEventsWithoutFilterCorrect() throws Exception {
        Mockito.when(eventPublicService.getPublicisedEventsWithFilter(eq(null), eq(null), eq(null),
                        eq(null), eq(null), Mockito.anyBoolean(), eq(null), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(eventShortDto));

        mvc.perform(get("/events")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$[0].annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$[0].eventDate", is(eventFullDto.getEventDate())));
    }

    @Test
    void getPublicisedEventsWithWrongSortGetBadRequest() throws Exception {
        String sort = "Wrong";

        mvc.perform(get("/events?sort={sort}", sort)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void getEventByIdCorrect() throws Exception {
        Long eventId = 1L;

        Mockito.when(eventPublicService.getEventById(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(eventFullDto);

        mvc.perform(get("/events/{eventId}", eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.eventDate", is(eventFullDto.getEventDate())));
    }
}