package ru.practicum;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Uri;

import java.time.Instant;

import static ru.practicum.ewm.stats.util.Constants.DATE_TIME_FORMATTER;

public class TestObjects {
    public EndpointHitDto endpointHitDto;
    public EndpointHitDto endpointHitDtoSecondEvent;
    public EndpointHit endpointHit;
    public EndpointHit endpointHitSecond;
    public ViewStatsDto viewStatsDtoAll;
    public ViewStatsDto viewStatsDtoUnique;

    public TestObjects() {
        endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp("ewm-main-service");
        endpointHitDto.setUri("/events/1");
        endpointHitDto.setIp("192.163.0.1");
        endpointHitDto.setTimestamp("2024-09-06 11:00:23");

        endpointHitDtoSecondEvent = new EndpointHitDto();
        endpointHitDtoSecondEvent.setApp("ewm-main-service");
        endpointHitDtoSecondEvent.setUri("/events/1");
        endpointHitDtoSecondEvent.setIp("192.163.0.1");
        endpointHitDtoSecondEvent.setTimestamp("2024-09-12 23:00:23");

        endpointHit = new EndpointHit();
        endpointHit.setId(1);
        endpointHit.setApp(new App(endpointHitDto.getApp()));
        endpointHit.setUri(new Uri(endpointHitDto.getUri()));
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setTimestamp(DATE_TIME_FORMATTER.parse(endpointHitDto.getTimestamp(), Instant::from));

        endpointHitSecond = new EndpointHit();
        endpointHitSecond.setId(2);
        endpointHitSecond.setApp(new App(endpointHitDtoSecondEvent.getApp()));
        endpointHitSecond.setUri(new Uri(endpointHitDtoSecondEvent.getUri()));
        endpointHitSecond.setIp(endpointHitDtoSecondEvent.getIp());
        endpointHitSecond.setTimestamp(DATE_TIME_FORMATTER.parse(endpointHitDtoSecondEvent.getTimestamp(), Instant::from));

        viewStatsDtoAll = new ViewStatsDto();
        viewStatsDtoAll.setApp(endpointHitDto.getApp());
        viewStatsDtoAll.setUri(endpointHitDto.getUri());
        viewStatsDtoAll.setHits(2);

        viewStatsDtoUnique = new ViewStatsDto();
        viewStatsDtoUnique.setApp(endpointHitDto.getApp());
        viewStatsDtoUnique.setUri(endpointHitDto.getUri());
        viewStatsDtoUnique.setHits(1);
    }
}
