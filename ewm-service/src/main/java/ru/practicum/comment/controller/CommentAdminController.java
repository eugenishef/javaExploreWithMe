package ru.practicum.comment.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.service.CommentAdminService;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentAdminController {

    public static final String COMMENT_ID_PATH = "/{comment-id}";

    final CommentAdminService commentAdminService;

    @DeleteMapping(COMMENT_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("comment-id") @Positive @NotNull Long commentId) {
        commentAdminService.deleteComment(commentId);
    }
}