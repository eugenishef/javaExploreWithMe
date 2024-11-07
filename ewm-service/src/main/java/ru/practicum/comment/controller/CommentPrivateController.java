package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.service.CommentPrivateService;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPrivateController {

    public static final String COMMENT_ID_PATH = "/{comment-id}";

    final CommentPrivateService commentPrivateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable @Positive @NotNull Long userId,
                                    @PathVariable @Positive @NotNull Long eventId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentPrivateService.createComment(userId, eventId, newCommentDto);
    }

    @PatchMapping(COMMENT_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable @Positive @NotNull Long userId,
                                    @PathVariable @Positive @NotNull Long eventId,
                                    @PathVariable("comment-id") @Positive @NotNull Long commentId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentPrivateService.updateComment(userId, eventId, commentId, newCommentDto);
    }

    @DeleteMapping(COMMENT_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive @NotNull Long userId,
                              @PathVariable @Positive @NotNull Long eventId,
                              @PathVariable("comment-id") @Positive @NotNull Long commentId) {
        commentPrivateService.deleteComment(userId, eventId, commentId);
    }
}