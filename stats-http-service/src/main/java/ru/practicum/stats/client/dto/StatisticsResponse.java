package ru.practicum.stats.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsResponse {
    private String app;
    private String uri;
    private String hits;
    private String timestamp;
}
