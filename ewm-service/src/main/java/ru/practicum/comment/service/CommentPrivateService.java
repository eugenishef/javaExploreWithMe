package ru.practicum.comment.service;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;

public interface CommentPrivateService {
    CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    void deleteComment(Long userId, Long eventId, Long commentId);
}