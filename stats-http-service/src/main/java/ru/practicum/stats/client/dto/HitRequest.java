package ru.practicum.stats.client.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitRequest {
    String app;
    String uri;
    String ip;
}
