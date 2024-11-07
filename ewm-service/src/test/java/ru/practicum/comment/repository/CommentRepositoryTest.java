package ru.practicum.comment.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.category.TestObjectsCategory;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.TestObjectsComment;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.model.Category;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.user.TestObjectsUser;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentRepositoryTest {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    private User user;
    private Category category;
    private Location location;
    private Event event;
    private Event secondEvent;
    private Comment comment;
    private Comment secondComment;
    private Comment commentToSecondEvent;

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);
        TestObjectsComment testObjectsComment = new TestObjectsComment(testObjectsUser, testObjectsEvent);

        user = testObjectsUser.user;
        event = testObjectsEvent.event;
        category = testObjectsCategory.category;
        location = testObjectsEvent.location;
        secondEvent = testObjectsEvent.secondEvent;
        comment = testObjectsComment.comment;
        secondComment = testObjectsComment.updatedComment;
        commentToSecondEvent = testObjectsComment.commentToSecondEvent;
    }

    @Test
    void saveCommentShouldReturnUserWithId() {
        user.setId(0L);
        user = userRepository.save(user);
        event.setInitiator(user);
        comment.setUser(user);

        category.setId(0L);
        category = categoryRepository.save(category);
        event.setCategory(category);

        location.setId(0L);
        location = locationRepository.save(location);
        event.setLocation(location);

        event.setId(0L);
        event = eventRepository.save(event);
        comment.setEvent(event);

        comment.setId(0L);
        Comment result = commentRepository.save(comment);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getId() > 0);
    }

    @Test
    void correctGetByEventId() {
        user.setId(0L);
        user = userRepository.save(user);
        event.setInitiator(user);
        secondEvent.setInitiator(user);
        comment.setUser(user);
        secondComment.setUser(user);
        commentToSecondEvent.setUser(user);

        category.setId(0L);
        category = categoryRepository.save(category);
        event.setCategory(category);
        secondEvent.setCategory(category);

        location.setId(0L);
        location = locationRepository.save(location);
        event.setLocation(location);
        secondEvent.setLocation(location);

        event.setId(0L);
        event = eventRepository.save(event);
        comment.setEvent(event);
        secondComment.setEvent(event);

        secondEvent.setId(0L);
        secondEvent = eventRepository.save(secondEvent);
        commentToSecondEvent.setEvent(secondEvent);

        comment.setId(0L);
        commentRepository.save(comment);
        secondComment.setId(0L);
        commentRepository.save(secondComment);
        commentToSecondEvent.setId(0L);
        commentRepository.save(commentToSecondEvent);

        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> commentPage = commentRepository.findAllByEventId(event.getId(), pageable);

        Assertions.assertNotNull(commentPage);
        Assertions.assertEquals(2, commentPage.size());
    }
}
