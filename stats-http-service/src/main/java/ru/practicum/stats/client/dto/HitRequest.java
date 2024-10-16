package ru.practicum.stats.client.dto;

import lombok.Data;

@Data
public class HitRequest {
    private String app;
    private String uri;
    private String ip;
}
