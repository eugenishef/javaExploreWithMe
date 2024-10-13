package ru.practicum.client.http;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.client.dto.HitRequest;
import ru.practicum.client.dto.StatisticsResponse;

import java.util.List;

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
