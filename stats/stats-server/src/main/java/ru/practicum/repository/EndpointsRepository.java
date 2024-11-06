package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.Instant;
import java.util.List;

@Repository
public interface EndpointsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value = """
        SELECT new ru.practicum.model.ViewStats(a.name, u.name, COUNT (eh.id))
        FROM EndpointHit eh
        JOIN eh.app a
        JOIN eh.uri u
        WHERE eh.timestamp >= ?1 AND eh.timestamp <= ?2
        GROUP BY a.name, u.name
    """
    )
    List<ViewStats> findViewStatsForAllUriAllIpsBetweenDates(Instant start, Instant end);

    @Query(value = """
        SELECT new ru.practicum.model.ViewStats(a.name, u.name, COUNT (eh.id))
        FROM EndpointHit eh
        JOIN eh.app a
        JOIN eh.uri u
        WHERE u.name IN ?3 AND eh.timestamp >= ?1 AND eh.timestamp <= ?2
        GROUP BY a.name, u.name
    """
    )
    List<ViewStats> findViewStatsForSpecifiedUriAllIpsBetweenDates(Instant start, Instant end, List<String> uris);

    @Query(value = """
        SELECT new ru.practicum.model.ViewStats(a.name, u.name, COUNT (DISTINCT eh.ip))
        FROM EndpointHit eh
        JOIN eh.app a
        JOIN eh.uri u
        WHERE eh.timestamp >= ?1 AND eh.timestamp <= ?2
        GROUP BY a.name, u.name
    """
    )
    List<ViewStats> findViewStatsForAllUriDistinctIpsBetweenDates(Instant start, Instant end);

    @Query(value = """
        SELECT new ru.practicum.model.ViewStats(a.name, u.name, COUNT (DISTINCT eh.ip))
        FROM EndpointHit eh
        JOIN eh.app a
        JOIN eh.uri u
        WHERE u.name IN ?3 AND eh.timestamp >= ?1 AND eh.timestamp <= ?2
        GROUP BY a.name, u.name
    """
    )
    List<ViewStats> findViewStatsForSpecifiedUriDistinctIpsBetweenDates(Instant start, Instant end, List<String> uris);
}
