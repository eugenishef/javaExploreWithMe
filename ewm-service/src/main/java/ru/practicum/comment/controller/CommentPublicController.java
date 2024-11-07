package ru.practicum.comment.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.service.CommentPublicService;
import ru.practicum.dto.comment.CommentDto;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPublicController {

    public static final String COMMENT_ID_PATH = "/{comment-id}";

    final CommentPublicService commentPublicService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComment(
            @PathVariable @Positive @NotNull Long eventId,
            @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive Integer size) {
        return commentPublicService.getEventComment(eventId, from, size);
    }

    @GetMapping(COMMENT_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable @Positive @NotNull Long eventId,
                                     @PathVariable("comment-id") @Positive @NotNull Long commentId) {
        return commentPublicService.getCommentById(eventId, commentId);
    }
}