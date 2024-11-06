package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.mapper.EndpointHitMapper;
import ru.practicum.dto.mapper.ViewStatsMapper;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Uri;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.AppsRepository;
import ru.practicum.repository.EndpointsRepository;
import ru.practicum.repository.UrisRepository;
import ru.practicum.util.exception.BadRequestException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ru.practicum.ewm.stats.util.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class StatsServiceBase implements StatsService {
    private final EndpointsRepository endpointsRepository;
    private final AppsRepository appsRepository;
    private final UrisRepository urisRepository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public EndpointHitDto createRecord(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.endpointHitDtoToEndpointHit(endpointHitDto);
        App app = endpointHit.getApp();
        Uri uri = endpointHit.getUri();

        Optional<App> optionalApp = appsRepository.findByName(app.getName());
        if (optionalApp.isPresent()) {
            app = optionalApp.get();
        } else {
            app = appsRepository.save(app);
        }

        Optional<Uri> optionalUri = urisRepository.findByName(uri.getName());
        if (optionalUri.isPresent()) {
            uri = optionalUri.get();
        } else {
            uri = urisRepository.save(uri);
        }

        endpointHit.setApp(app);
        endpointHit.setUri(uri);
        endpointHit = endpointsRepository.save(endpointHit);
        return endpointHitMapper.endpointHitToEndpointHitDto(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(String startString, String endString, List<String> uris, boolean unique) {
        Instant start = DATE_TIME_FORMATTER.parse(java.net.URLDecoder.decode(startString, StandardCharsets.UTF_8),
                Instant::from);
        Instant end = DATE_TIME_FORMATTER.parse(java.net.URLDecoder.decode(endString, StandardCharsets.UTF_8),
                Instant::from);
        if (end.isBefore(start)) {
            throw new BadRequestException("Start should be before end");
        }
        List<ViewStats> viewStatsList;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                viewStatsList = endpointsRepository.findViewStatsForAllUriDistinctIpsBetweenDates(start, end);
            } else {
                viewStatsList = endpointsRepository.findViewStatsForAllUriAllIpsBetweenDates(start, end);
            }
        } else {
            if (unique) {
                viewStatsList = endpointsRepository.findViewStatsForSpecifiedUriDistinctIpsBetweenDates(start, end, uris);
            } else {
                viewStatsList = endpointsRepository.findViewStatsForSpecifiedUriAllIpsBetweenDates(start, end, uris);
            }
        }

        return viewStatsList.stream()
                .map(viewStatsMapper::viewStatsToViewStatsDto)
                .sorted(Comparator.comparing(ViewStatsDto::getHits).reversed())
                .toList();
    }
}
