package ru.practicum.stats.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import ru.practicum.stats.client.dto.*;
import ru.practicum.stats.model.Statistics;
import ru.practicum.stats.repository.StatisticsRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public Statistics logHit(@Valid HitRequest hitRequest) {
        log.info("Logging hit for request: {}", hitRequest);

        Statistics stats = new Statistics();
        stats.setApp(hitRequest.getApp());
        stats.setEndpoint(hitRequest.getUri());
        stats.setIp(hitRequest.getIp());
        stats.setRequestTime(LocalDateTime.now());

        Statistics savedStats = statisticsRepository.save(stats);
        log.info("Hit logged successfully: {}", savedStats);

        return savedStats;
    }

    public List<StatisticsResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Fetching statistics from {} to {}, URIs: {}, Unique: {}", start, end, uris, unique);

        List<Statistics> statsList = statisticsRepository.findAll();
        log.debug("Fetched {} records from the database", statsList.size());

        List<Statistics> filteredStats = statsList.stream()
                .filter(stat -> !stat.getRequestTime().isBefore(start) && !stat.getRequestTime().isAfter(end))
                .filter(stat -> uris == null || uris.isEmpty() || uris.contains(stat.getEndpoint()))
                .collect(Collectors.toList());

        if (unique) {
            filteredStats = filteredStats.stream()
                    .distinct()
                    .collect(Collectors.toList());
        }

        List<StatisticsResponse> responseStats = filteredStats.stream()
                .collect(Collectors.groupingBy(Statistics::getEndpoint))
                .entrySet()
                .stream()
                .map(entry -> new StatisticsResponse("ewm-main-service", entry.getKey(), String.valueOf(entry.getValue().size()), null))
                .collect(Collectors.toList());

        log.info("Filtered and grouped statistics: {}", responseStats);
        return responseStats;
    }

    public void logRequest(String endpoint) {
        log.info("Logging request for endpoint: {}", endpoint);

        Statistics stats = new Statistics();
        stats.setEndpoint(endpoint);
        stats.setRequestTime(LocalDateTime.now());

        statisticsRepository.save(stats);
        log.info("Request logged for endpoint: {}", endpoint);
    }
}
