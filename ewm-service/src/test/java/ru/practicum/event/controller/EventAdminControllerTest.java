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
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.event.service.EventAdminService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventAdminController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventAdminControllerTest {
    @MockBean
    private final EventAdminService eventAdminService;
    @MockBean
    private final StatsClient statsClient;

    private final ObjectMapper mapper;
    private final MockMvc mvc;

    private EventFullDto eventFullDto;
    private UpdateEventAdminRequest updateEventAdminRequest;

    @BeforeEach
    void setUp() {
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);

        eventFullDto = testObjectsEvent.eventFullDto;
        updateEventAdminRequest = testObjectsEvent.updateEventAdminRequest;
    }

    @Test
    void getAllEventsWithoutAnyFilterCorrect() throws Exception {
        Mockito.when(eventAdminService.getAllEventsWithFilter(eq(null), eq(null), eq(null),
                        eq(null), eq(null), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(eventFullDto));

        mvc.perform(get("/admin/events")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$[0].annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$[0].eventDate", is(eventFullDto.getEventDate())));
    }

    @Test
    void getAllEventsWithWrongUserIdsGetBadRequest() throws Exception {
        String userIds = "1,-1";

        mvc.perform(get("/admin/events?users={userIds}", userIds)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));

    }

    @Test
    void updateCorrectEventByAdmin() throws Exception {
        Mockito.when(eventAdminService.updateEventByAdmin(Mockito.anyLong(), Mockito.any()))
                .thenReturn(eventFullDto);

        mvc.perform(patch("/admin/events/{eventId}", 1L)
                        .content(mapper.writeValueAsString(updateEventAdminRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.eventDate", is(eventFullDto.getEventDate())));
    }

    @Test
    void updateWrongStateEventEventByAdminGetBadRequest() throws Exception {
        updateEventAdminRequest.setStateAction("Wrong");

        mvc.perform(patch("/admin/events/{eventId}", 1L)
                        .content(mapper.writeValueAsString(updateEventAdminRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }
}