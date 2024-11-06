package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsIntegrationTests {

    private final TestRestTemplate testRestTemplate;
    private EndpointHitDto endpointHitDto;
    private EndpointHitDto endpointHitDtoSecondEvent;
    private ViewStatsDto viewStatsDtoAll;
    private ViewStatsDto viewStatsDtoUnique;

    @BeforeEach
    void beforeEach() {
        TestObjects testObjects = new TestObjects();

        endpointHitDto = testObjects.endpointHitDto;
        endpointHitDtoSecondEvent = testObjects.endpointHitDtoSecondEvent;
        viewStatsDtoAll = testObjects.viewStatsDtoAll;
        viewStatsDtoUnique = testObjects.viewStatsDtoUnique;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createUserFullIntegrationTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto, headers);

        ResponseEntity<EndpointHitDto> response = testRestTemplate.exchange(
                "http://localhost:9090/hit", HttpMethod.POST, request, EndpointHitDto.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        EndpointHitDto actual = response.getBody();
        assertThat(actual)
                .usingRecursiveComparison().isEqualTo(endpointHitDto);
    }

    @Test
    void getViewStatsAllTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto, headers);

        ResponseEntity<EndpointHitDto> response = testRestTemplate.exchange(
                "http://localhost:9090/hit", HttpMethod.POST, request, EndpointHitDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        request = new HttpEntity<>(endpointHitDtoSecondEvent, headers);
        response = testRestTemplate.exchange(
                "http://localhost:9090/hit", HttpMethod.POST, request, EndpointHitDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<List<ViewStatsDto>> getResponse = testRestTemplate.exchange(
                "http://localhost:9090/stats?start=2021-01-21+12%3A00%3A00&end=2025-01-21+12%3A00%3A00",
                HttpMethod.GET, null, new ParameterizedTypeReference<>(){}
        );

        Assertions.assertNotNull(getResponse);
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Assertions.assertNotNull(getResponse.getBody());

        List<ViewStatsDto> usersList = getResponse.getBody();
        assertThat(usersList.getFirst())
                .usingRecursiveComparison().isEqualTo(viewStatsDtoAll);
    }

    @Test
    void getViewStatsUniqueTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto, headers);

        ResponseEntity<EndpointHitDto> response = testRestTemplate.exchange(
                "http://localhost:9090/hit", HttpMethod.POST, request, EndpointHitDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        request = new HttpEntity<>(endpointHitDtoSecondEvent, headers);
        response = testRestTemplate.exchange(
                "http://localhost:9090/hit", HttpMethod.POST, request, EndpointHitDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<List<ViewStatsDto>> getResponse = testRestTemplate.exchange(
                "http://localhost:9090/stats?start=2021-01-21+12%3A00%3A00&end=2025-01-21+12%3A00%3A00&unique=true",
                HttpMethod.GET, null, new ParameterizedTypeReference<>(){}
        );

        Assertions.assertNotNull(getResponse);
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Assertions.assertNotNull(getResponse.getBody());

        List<ViewStatsDto> usersList = getResponse.getBody();
        assertThat(usersList.getFirst())
                .usingRecursiveComparison().isEqualTo(viewStatsDtoUnique);
    }
}
