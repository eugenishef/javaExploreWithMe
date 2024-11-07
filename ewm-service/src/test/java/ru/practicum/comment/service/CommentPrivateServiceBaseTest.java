package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.category.TestObjectsCategory;
import ru.practicum.comment.TestObjectsComment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapperImpl;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.user.TestObjectsUser;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.NotFoundException;

import java.util.Optional;

@SpringBootTest(classes = {CommentPrivateServiceBase.class, CommentMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentPrivateServiceBaseTest {

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final EventRepository eventRepository;

    @MockBean
    private final CommentRepository commentRepository;

    private final CommentPrivateService commentPrivateService;

    private Comment comment;
    private CommentDto commentDto;
    private NewCommentDto newCommentDto;
    private NewCommentDto updateCommentDto;
    public Comment updatedComment;
    public CommentDto updatedCommentDto;

    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);
        TestObjectsComment testObjectsComment = new TestObjectsComment(testObjectsUser, testObjectsEvent);

        comment = testObjectsComment.comment;
        commentDto = testObjectsComment.commentDto;
        newCommentDto = testObjectsComment.newCommentDto;
        updateCommentDto = testObjectsComment.updateCommentDto;
        updatedComment = testObjectsComment.updatedComment;
        updatedCommentDto = testObjectsComment.updatedCommentDto;

        user = testObjectsUser.user;
        event = testObjectsEvent.event;
    }

    @Test
    void createCorrectComment() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(comment);

        CommentDto result = commentPrivateService.createComment(1L, 1L, newCommentDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(commentDto, result);
    }

    @Test
    void createCommentUserNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(comment);

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.createComment(1L, 1L, newCommentDto);
        });
    }

    @Test
    void createCommentEventNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(comment);

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.createComment(1L, 1L, newCommentDto);
        });
    }

    @Test
    void updateCorrectComment() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(comment));
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(updatedComment);

        CommentDto result = commentPrivateService.updateComment(1L, 1L, 1L, updateCommentDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updatedCommentDto, result);
    }

    @Test
    void updateCommentUserNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(comment));
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(updatedComment);

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.updateComment(1L, 1L, 1L, newCommentDto);
        });
    }

    @Test
    void updateCommentEventNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(comment));
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(updatedComment);

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.updateComment(1L, 1L, 1L, newCommentDto);
        });
    }

    @Test
    void updateCommentCommentNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(updatedComment);

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.updateComment(1L, 1L, 1L, newCommentDto);
        });
    }

    @Test
    void deleteCorrectComment() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(comment));

        commentPrivateService.deleteComment(1L, 1L, 1L);

        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteCommentUserNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(comment));

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.deleteComment(1L, 1L, 1L);
        });
    }

    @Test
    void deleteCommentEventNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(comment));

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.deleteComment(1L, 1L, 1L);
        });
    }

    @Test
    void deleteCommentCommentNotFoundException() throws Exception {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(eventRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPrivateService.deleteComment(1L, 1L, 1L);
        });
    }
}