package ru.practicum.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Uri;

import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {Instant.class, App.class, Uri.class})
public interface EndpointHitMapper {

    @Mapping(target = "timestamp",
            expression = "java(ru.practicum.ewm.stats.util.Constants.DATE_TIME_FORMATTER" +
                    ".parse(endpointHitDto.getTimestamp(), Instant::from))")
    @Mapping(target = "app", expression = "java(new App(endpointHitDto.getApp()))")
    @Mapping(target = "uri", expression = "java(new Uri(endpointHitDto.getUri()))")
    EndpointHit endpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto);

    @Mapping(target = "timestamp", expression = "java(ru.practicum.ewm.stats.util.Constants.DATE_TIME_FORMATTER" +
            ".format(endpointHit.getTimestamp()))")
    @Mapping(target = "app", expression = "java(endpointHit.getApp().getName())")
    @Mapping(target = "uri", expression = "java(endpointHit.getUri().getName())")
    EndpointHitDto endpointHitToEndpointHitDto(EndpointHit endpointHit);
}
