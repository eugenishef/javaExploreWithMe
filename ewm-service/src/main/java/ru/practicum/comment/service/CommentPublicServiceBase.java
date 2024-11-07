package ru.practicum.comment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.exception.NotFoundException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPublicServiceBase implements CommentPublicService {
    final CommentRepository commentRepository;
    final EventRepository eventRepository;

    final CommentMapper commentMapper;

    @Override
    public List<CommentDto> getEventComment(Long eventId, Integer from, Integer size) {
        checkEventByIdOrThrowNotFoundException(eventId);
        Pageable pageable = PageRequest.of(from, size);
        return commentRepository.findAllByEventId(eventId, pageable).stream()
                .map(commentMapper::commentToCommentDto)
                .toList();
    }

    @Override
    public CommentDto getCommentById(Long eventId, Long commentId) {
        checkEventByIdOrThrowNotFoundException(eventId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(commentId, Comment.class));
        if (!Objects.equals(comment.getEvent().getId(), eventId))
            throw new ConflictException("Event id not equal to comment event id");
        return commentMapper.commentToCommentDto(comment);
    }

    private void checkEventByIdOrThrowNotFoundException(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId, Event.class));
    }
}