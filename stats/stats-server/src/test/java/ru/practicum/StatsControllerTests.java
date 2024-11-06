package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.controller.StatsController;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsControllerTests {

    @MockBean
    private StatsService statsService;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private EndpointHitDto endpointHitDto;
    private ViewStatsDto viewStatsDtoAll;

    @BeforeEach
    void beforeEach() {
        TestObjects testObjects = new TestObjects();

        endpointHitDto = testObjects.endpointHitDto;
        viewStatsDtoAll = testObjects.viewStatsDtoAll;
    }

    @Test
    void saveEndpointHitTest() throws Exception {
        Mockito.when(statsService.createRecord(Mockito.any()))
                .thenReturn(endpointHitDto);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.app", is(endpointHitDto.getApp())))
                .andExpect(jsonPath("$.ip", is(endpointHitDto.getIp())))
                .andExpect(jsonPath("$.uri", is(endpointHitDto.getUri())));
    }

    @Test
    void getViewStats() throws Exception {
        Mockito.when(statsService.getStats(Mockito.any(), Mockito.any(), eq(null), eq(false)))
                .thenReturn(List.of(viewStatsDtoAll));

        mvc.perform(get("/stats?start=2022-01-21+12%3A00%3A00&end=2024-01-21+12%3A00%3A00")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].app", is(viewStatsDtoAll.getApp())))
                .andExpect(jsonPath("$[0].uri", is(viewStatsDtoAll.getUri())))
                .andExpect(jsonPath("$[0].hits", is(viewStatsDtoAll.getHits()), Long.class));
    }
}
