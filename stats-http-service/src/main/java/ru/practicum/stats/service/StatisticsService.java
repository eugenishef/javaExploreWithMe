package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.dto.HitRequest;
import ru.practicum.client.dto.StatisticsResponse;
import ru.practicum.stats.model.Statistics;
import ru.practicum.stats.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public Statistics logHit(HitRequest hitRequest) {
        Statistics stats = new Statistics();
        stats.setApp(hitRequest.getApp());
        stats.setEndpoint(hitRequest.getUri());
        stats.setIp(hitRequest.getIp());
        stats.setRequestTime(LocalDateTime.now());

        return statisticsRepository.save(stats);
    }

    public List<StatisticsResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Statistics> statsList = statisticsRepository.findAll();

        return statsList.stream()
                .filter(stat -> !stat.getRequestTime().isBefore(start) && !stat.getRequestTime().isAfter(end))
                .filter(stat -> uris == null || uris.isEmpty() || uris.contains(stat.getEndpoint()))
                .collect(Collectors.groupingBy(Statistics::getEndpoint))
                .entrySet()
                .stream()
                .map(entry -> new StatisticsResponse("ewm-main-service", entry.getKey(), String.valueOf(entry.getValue().size()), null))
                .collect(Collectors.toList());
    }

    public void logRequest(String endpoint) {
        Statistics stats = new Statistics();
        stats.setEndpoint(endpoint);
        stats.setRequestTime(LocalDateTime.now());
        statisticsRepository.save(stats);
    }

    public List<Statistics> getAllRequests() {
        return statisticsRepository.findAll();
    }
}
