package ru.practicum.stats.client.http;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import ru.practicum.stats.client.dto.*;

@Component
public class StatisticsHttpClient {

    private final RestTemplate restTemplate;

    public StatisticsHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StatisticsResponse logHit(HitRequest hitRequest) {
        return restTemplate.postForObject("http://localhost:9090/hit", hitRequest, StatisticsResponse.class);
    }

    public List<StatisticsResponse> getStats(String start, String end, List<String> uris, boolean unique) {
        String url = String.format("http://localhost:9090/stats?start=%s&end=%s&uris=%s&unique=%s",
                start, end, String.join(",", uris), unique);
        return restTemplate.getForObject(url, List.class);
    }
}
