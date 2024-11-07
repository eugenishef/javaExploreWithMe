package ru.practicum.comment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.exception.NotFoundException;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPrivateServiceBase implements CommentPrivateService {
    final CommentRepository commentRepository;
    final UserRepository userRepository;
    final EventRepository eventRepository;

    final CommentMapper commentMapper;

    @Override
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = findUserByIdOrThrowNotFoundException(userId);
        Event event = findEventByIdOrThrowNotFoundException(eventId);
        Comment comment = commentMapper.newCommentDtoToComment(newCommentDto);
        comment.setUser(user);
        comment.setEvent(event);
        comment.setCreated(Instant.now());
        comment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        findUserByIdOrThrowNotFoundException(userId);
        findEventByIdOrThrowNotFoundException(eventId);
        Comment comment = findCommentByIdOrThrowNotFoundException(commentId);
        if (!Objects.equals(comment.getUser().getId(), userId))
            throw new ConflictException("Only comment creator could update comment");
        if (!Objects.equals(comment.getEvent().getId(), eventId))
            throw new ConflictException("Event id not equal to comment event id");
        commentMapper.updateCommentWithNewCommentDto(newCommentDto, comment);
        comment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        findUserByIdOrThrowNotFoundException(userId);
        findEventByIdOrThrowNotFoundException(eventId);
        Comment comment = findCommentByIdOrThrowNotFoundException(commentId);
        if (!Objects.equals(comment.getUser().getId(), userId))
            throw new ConflictException("Only comment creator could delete comment");
        commentRepository.deleteById(commentId);
    }

    private User findUserByIdOrThrowNotFoundException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
    }

    private Event findEventByIdOrThrowNotFoundException(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId, Event.class));
    }

    private Comment findCommentByIdOrThrowNotFoundException(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(commentId, Comment.class));
    }

}