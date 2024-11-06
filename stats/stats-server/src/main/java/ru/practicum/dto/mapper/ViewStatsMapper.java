package ru.practicum.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.model.ViewStats;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ViewStatsMapper {
    ViewStatsDto viewStatsToViewStatsDto(ViewStats viewStats);
}
