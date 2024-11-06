package ru.practicum.util.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.model.Event;

import java.time.Instant;

import static ru.practicum.config.EWMServiceAppConfig.DATE_TIME_FORMATTER;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {LocationMapper.class, UserMapper.class, CategoryMapper.class})
public interface EventMapper {

    @Named("stringToInstant")
    default Instant stringToInstant(String instantString) {
        return (instantString != null && !instantString.isBlank()) ?
                DATE_TIME_FORMATTER.parse(instantString, Instant::from) : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "stringToInstant")
    Event newEventDtoToEvent(NewEventDto newEventDto);

    @Named("instantToString")
    default String instantToString(Instant instant) {
        return (instant != null) ? DATE_TIME_FORMATTER.format(instant) : null;
    }

    @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "instantToString")
    @Mapping(target = "createdOn", source = "createdOn", qualifiedByName = "instantToString")
    @Mapping(target = "publishedOn", source = "publishedOn", qualifiedByName = "instantToString")
    EventFullDto eventToEventFullDto(Event event);

    @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "instantToString")
    EventShortDto eventToeventShortDto(Event event);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "stringToInstant")
    @Mapping(target = "state", ignore = true)
    void updateEventUserRequestIgnoringLocationAndCategoryId(UpdateEventUserRequest updateEventUserRequest,
                                                             @MappingTarget Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "eventDate", source = "eventDate", qualifiedByName = "stringToInstant")
    @Mapping(target = "state", ignore = true)
    void updateEventAdminRequestIgnoringLocationAndCategoryId(UpdateEventAdminRequest updateEventAdminRequest,
                                                              @MappingTarget Event event);
}
