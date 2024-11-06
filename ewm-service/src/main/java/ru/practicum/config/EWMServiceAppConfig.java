package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatsClient;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class EWMServiceAppConfig {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public static final String APP_NAME = "evm-service";

    @Bean
    public StatsClient statsClient() {
        return new StatsClient();
    }
}
