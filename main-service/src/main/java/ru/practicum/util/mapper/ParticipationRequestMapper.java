package ru.practicum.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.event.request.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, EventMapper.class})
public interface ParticipationRequestMapper {

    @Mapping(target = "created", source = "created", qualifiedByName = "instantToString")
    @Mapping(target = "event", expression = "java(participationRequest.getEvent().getId())")
    @Mapping(target = "requester", expression = "java(participationRequest.getRequester().getId())")
    ParticipationRequestDto participationRequestToParticipationRequestDto(ParticipationRequest participationRequest);
}
