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
import ru.practicum.dto.comment.CommentMapperImpl;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.Comment;
import ru.practicum.user.TestObjectsUser;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.NotFoundException;

import java.util.Optional;

@SpringBootTest(classes = {CommentAdminServiceBase.class, CommentMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentAdminServiceBaseTest {

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final EventRepository eventRepository;

    @MockBean
    private final CommentRepository commentRepository;

    private final CommentAdminService commentAdminService;

    private Comment comment;

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);
        TestObjectsComment testObjectsComment = new TestObjectsComment(testObjectsUser, testObjectsEvent);

        comment = testObjectsComment.comment;
    }

    @Test
    void deleteCorrectComment() throws Exception {
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(comment));

        commentAdminService.deleteComment(1L);

        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteCommentUserNotFoundException() throws Exception {
        Mockito.when(commentRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            commentAdminService.deleteComment(1L);
            ;
        });
    }
}
