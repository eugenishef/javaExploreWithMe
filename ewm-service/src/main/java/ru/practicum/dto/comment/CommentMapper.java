package ru.practicum.dto.comment;

import org.mapstruct.*;
import ru.practicum.model.Comment;

import java.time.Instant;

import static ru.practicum.config.EWMServiceAppConfig.DATE_TIME_FORMATTER;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "event", ignore = true)
    Comment newCommentDtoToComment(NewCommentDto newCommentDto);

    @Mapping(target = "created", source = "created", qualifiedByName = "instantToString")
    @Mapping(target = "eventId", expression = "java(comment.getEvent().getId())")
    @Mapping(target = "userId", expression = "java(comment.getUser().getId())")
    CommentDto commentToCommentDto(Comment comment);

    @Named("instantToString")
    default String instantToString(Instant instant) {
        return (instant != null) ? DATE_TIME_FORMATTER.format(instant) : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "event", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentWithNewCommentDto(NewCommentDto newCommentDto, @MappingTarget Comment comment);
}
