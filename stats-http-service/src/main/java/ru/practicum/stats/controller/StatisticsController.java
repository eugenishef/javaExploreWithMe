package ru.practicum.stats.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ru.practicum.stats.client.dto.*;
import ru.practicum.stats.model.Statistics;
import ru.practicum.stats.service.StatisticsService;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public ResponseEntity<StatisticsResponse> logHit(@Valid @RequestBody HitRequest hitRequest) {
        log.info("Received request to log hit: {}", hitRequest);
        Statistics stats = statisticsService.logHit(hitRequest);

        String formattedTimestamp = stats.getRequestTime().format(FORMATTER);
        StatisticsResponse response = new StatisticsResponse(stats.getApp(), stats.getEndpoint(), stats.getIp(), formattedTimestamp);

        log.info("Hit logged successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatisticsResponse>> getStats(
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") boolean unique) {

        statisticsService.logRequest("/stats");

        LocalDateTime startDateTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(end, FORMATTER);

        List<StatisticsResponse> stats = statisticsService.getStats(startDateTime, endDateTime, uris, unique);

        log.info("Fetched {} records", stats.size());
        return ResponseEntity.ok(stats);
    }
}
