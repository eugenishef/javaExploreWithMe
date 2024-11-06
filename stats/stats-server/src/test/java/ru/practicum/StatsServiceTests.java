package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.dto.mapper.EndpointHitMapperImpl;
import ru.practicum.dto.mapper.ViewStatsMapperImpl;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.AppsRepository;
import ru.practicum.repository.EndpointsRepository;
import ru.practicum.repository.UrisRepository;
import ru.practicum.service.StatsService;
import ru.practicum.service.StatsServiceBase;

import java.util.Optional;

@SpringBootTest(classes = {StatsServiceBase.class, EndpointHitMapperImpl.class, ViewStatsMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServiceTests {
    @MockBean
    private EndpointsRepository endpointsRepository;
    @MockBean
    private AppsRepository appsRepository;
    @MockBean
    private UrisRepository urisRepository;
    private final StatsService statsService;

    private EndpointHitDto endpointHitDto;
    private EndpointHit endpointHit;

    @BeforeEach
    void beforeEach() {
        TestObjects testObjects = new TestObjects();
        endpointHitDto = testObjects.endpointHitDto;
        endpointHit = testObjects.endpointHit;
    }

    @Test
    void saveEndpointHitTest() {
        Mockito.when(appsRepository.findByName(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(appsRepository.save(Mockito.any()))
                .thenReturn(endpointHit.getApp());
        Mockito.when(urisRepository.findByName(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(urisRepository.save(Mockito.any()))
                .thenReturn(endpointHit.getUri());
        Mockito.when(endpointsRepository.save(Mockito.any()))
                .thenReturn(endpointHit);

        EndpointHitDto actual = statsService.createRecord(endpointHitDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(endpointHitDto, actual);
    }


}
