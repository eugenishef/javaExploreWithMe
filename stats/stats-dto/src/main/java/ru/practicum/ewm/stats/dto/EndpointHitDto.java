package ru.practicum.ewm.stats.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static ru.practicum.ewm.stats.util.Constants.DATE_TIME_FORMATTER;

@Data
@NoArgsConstructor
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;

    public EndpointHitDto(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = DATE_TIME_FORMATTER.format(Instant.now());
    }
}