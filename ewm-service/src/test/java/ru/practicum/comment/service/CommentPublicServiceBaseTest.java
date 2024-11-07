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
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.user.TestObjectsUser;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {CommentPublicServiceBase.class, CommentMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentPublicServiceBaseTest {

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final EventRepository eventRepository;

    @MockBean
    private final CommentRepository commentRepository;

    private final CommentPublicService commentPublicService;

    private Comment comment;
    private CommentDto commentDto;
    public Comment updatedComment;
    public CommentDto updatedCommentDto;

    private Event event;

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);
        TestObjectsComment testObjectsComment = new TestObjectsComment(testObjectsUser, testObjectsEvent);

        comment = testObjectsComment.comment;
        commentDto = testObjectsComment.commentDto;
        updatedComment = testObjectsComment.updatedComment;
        updatedCommentDto = testObjectsComment.updatedCommentDto;

        event = testObjectsEvent.event;
    }

    @Test
    void getCommentByIdCorrect() {
        Mockito.when(eventRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        CommentDto result = commentPublicService.getCommentById(1L, 1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(commentDto, result);
    }

    @Test
    void getCommentByIdNotCorrectEvent() {
        long notCorrectEventId = comment.getEvent().getId() + 1;
        Event newEvent = new Event();
        newEvent.setId(notCorrectEventId);

        Mockito.when(eventRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(newEvent));
        Mockito.when(commentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        Assertions.assertThrows(ConflictException.class, () -> {
            commentPublicService.getCommentById(notCorrectEventId, 1L);
        });
    }

    @Test
    void getCommentByEventIdCorrect() {
        Mockito.when(eventRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(event));
        Mockito.when(commentRepository.findAllByEventId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(comment));

        List<CommentDto> result = commentPublicService.getEventComment(1L, 0, 10);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(commentDto, result.getFirst());
    }

    @Test
    void getCommentByEventIdNotFoundEvent() {
        Mockito.when(eventRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(commentRepository.findAllByEventId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(comment));

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentPublicService.getEventComment(1L, 0, 10);
        });
    }
}
