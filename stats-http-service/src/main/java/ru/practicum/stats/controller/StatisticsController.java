package ru.practicum.stats.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.dto.HitRequest;
import ru.practicum.client.dto.StatisticsResponse;
import ru.practicum.stats.model.Statistics;
import ru.practicum.stats.service.StatisticsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public ResponseEntity<StatisticsResponse> logHit(@Valid @RequestBody HitRequest hitRequest) {
        Statistics stats = statisticsService.logHit(hitRequest);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = stats.getRequestTime().format(formatter);

        StatisticsResponse response = new StatisticsResponse(stats.getApp(), stats.getEndpoint(), stats.getIp(), formattedTimestamp);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatisticsResponse>> getStats(
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") boolean unique) {

        LocalDateTime startDateTime = LocalDateTime.parse(start);
        LocalDateTime endDateTime = LocalDateTime.parse(end);

        List<StatisticsResponse> stats = statisticsService.getStats(startDateTime, endDateTime, uris, unique);

        return ResponseEntity.ok(stats);
    }
}
