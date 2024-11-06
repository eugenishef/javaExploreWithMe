package ru.practicum;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static ru.practicum.ewm.stats.util.Constants.DATE_TIME_FORMATTER;

public class StatsClient {

    private final RestClient restClient;
    private static final String BASE_URL = "http://stats-server:9090";

    public StatsClient() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public EndpointHitDto createRecord(EndpointHitDto endpointHitDto) {
        return restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(endpointHitDto)
                .retrieve()
                .body(EndpointHitDto.class);
    }

    public List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, Boolean unique) {
        String startStringToUrl = java.net.URLEncoder.encode(DATE_TIME_FORMATTER.format(start), StandardCharsets.UTF_8);
        String endStringToUrl = java.net.URLEncoder.encode(DATE_TIME_FORMATTER.format(end), StandardCharsets.UTF_8);
        StringBuilder uriBuilder = new StringBuilder("/stats?");
        uriBuilder.append("start=").append(startStringToUrl);
        uriBuilder.append("&end=").append(endStringToUrl);
        if (uris != null && !uris.isEmpty()) {
            uriBuilder.append("&uris=").append(String.join(",", uris));
        }
        if (unique != null) {
            uriBuilder.append("&unique=").append(unique);
        }

        return restClient.get()
                .uri(uriBuilder.toString())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris) {
        return getStats(start, end, uris, null);
    }

    public List<ViewStatsDto> getStats(Instant start, Instant end, Boolean unique) {
        return getStats(start, end, null, unique);
    }

    public List<ViewStatsDto> getStats(Instant start, Instant end) {
        return getStats(start, end, null, null);
    }
}
