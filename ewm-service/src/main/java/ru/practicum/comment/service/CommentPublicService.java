package ru.practicum.comment.service;

import ru.practicum.dto.comment.CommentDto;

import java.util.List;

public interface CommentPublicService {
    List<CommentDto> getEventComment(Long eventId, Integer from, Integer size);

    CommentDto getCommentById(Long eventId, Long commentId);
}