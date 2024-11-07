package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.practicum.StatsClient;
import ru.practicum.category.TestObjectsCategory;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.user.TestObjectsUser;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql"}),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:test-data-after.sql"})
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentIntegrationTest {

    private final TestRestTemplate testRestTemplate;

    private HttpHeaders headers;

    @MockBean
    private final StatsClient statsClient;

    private String baseUrl = "http://localhost:8080";
    private String createUserApi = "/admin/users";
    private String createCategoryApi = "/admin/categories";
    private NewUserRequest newUserRequest;
    private NewCategoryDto newCategoryDto;
    private NewEventDto newEventDto;
    private NewEventDto secondNewEventDto;
    private NewCommentDto newCommentDto;
    private NewCommentDto secondComment;
    private NewCommentDto commentToSecondEvent;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        TestObjectsUser testObjectsUser = new TestObjectsUser();
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);
        TestObjectsComment testObjectsComment = new TestObjectsComment(testObjectsUser, testObjectsEvent);
        newUserRequest = testObjectsUser.newUserRequest;
        newCategoryDto = testObjectsCategory.newCategoryDto;
        newEventDto = testObjectsEvent.newEventDto;
        secondNewEventDto = testObjectsEvent.secondNewEventDto;

        newCommentDto = testObjectsComment.newCommentDto;
        commentDto = testObjectsComment.commentDto;
        secondComment = testObjectsComment.updateCommentDto;
        commentToSecondEvent = testObjectsComment.newCommentToSecondEvent;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createCommentFullIntegrationTest() {
        UserDto createdUser = createUser(newUserRequest);
        createCategory(newCategoryDto);
        EventFullDto createdEventDto = createEvent(newEventDto, createdUser.getId());

        CommentDto createdCommentDto = createComment(newCommentDto, createdUser.getId(), createdEventDto.getId());

        assertThat(createdCommentDto)
                .usingRecursiveComparison().ignoringFields("id", "created").isEqualTo(commentDto);
    }

    @Test
    void deleteCommentFullIntegrationTest() {
        UserDto createdUser = createUser(newUserRequest);
        createCategory(newCategoryDto);
        EventFullDto createdEventDto = createEvent(newEventDto, createdUser.getId());

        CommentDto createdCommentDto = createComment(newCommentDto, createdUser.getId(), createdEventDto.getId());

        HttpEntity<Void> commentDeleteRequest = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteCommentResponse = testRestTemplate.exchange(
                baseUrl + deleteCommentApi(createdUser.getId(), createdEventDto.getId(), createdCommentDto.getId()),
                HttpMethod.DELETE, commentDeleteRequest, Void.class
        );

        Assertions.assertNotNull(deleteCommentResponse);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteCommentResponse.getStatusCode());
    }

    @Test
    void getCommentsFullIntegrationTest() {
        UserDto createdUser = createUser(newUserRequest);
        createCategory(newCategoryDto);
        EventFullDto createdFirstEventDto = createEvent(newEventDto, createdUser.getId());
        EventFullDto createdSecondEventDto = createEvent(secondNewEventDto, createdUser.getId());

        CommentDto createdFirstCommentDto =
                createComment(newCommentDto, createdUser.getId(), createdFirstEventDto.getId());
        CommentDto createdSecondCommentDto =
                createComment(secondComment, createdUser.getId(), createdFirstEventDto.getId());
        CommentDto createdThirdCommentDto =
                createComment(commentToSecondEvent, createdUser.getId(), createdSecondEventDto.getId());

        HttpEntity<Void> getCommentToFirstEventRequest = new HttpEntity<>(headers);
        ResponseEntity<List<CommentDto>> getCommentByEventResponse = testRestTemplate.exchange(
                baseUrl + getCommentsByEventCommentApi(createdFirstEventDto.getId()), HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                }
        );

        Assertions.assertNotNull(getCommentByEventResponse);
        Assertions.assertEquals(HttpStatus.OK, getCommentByEventResponse.getStatusCode());

        Assertions.assertNotNull(getCommentByEventResponse.getBody());
        List<CommentDto> comments = getCommentByEventResponse.getBody();
        assertThat(comments.get(0))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(createdFirstCommentDto);
        assertThat(comments.get(1))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(createdSecondCommentDto);
    }

    private String createEventApi(long userId) {
        return String.format("/users/%d/events", userId);
    }

    private String createCommentApi(long userId, long eventId) {
        return String.format("/users/%d/events/%d/comments", userId, eventId);
    }

    private String deleteCommentApi(long userId, long eventId, long commentId) {
        return String.format("/users/%d/events/%d/comments/%d", userId, eventId, commentId);
    }

    private String getCommentsByEventCommentApi(long eventId) {
        return String.format("/events/%d/comments", eventId);
    }

    private UserDto createUser(NewUserRequest newUserRequest) {
        HttpEntity<NewUserRequest> createUserRequest = new HttpEntity<>(newUserRequest, headers);

        ResponseEntity<UserDto> createUserResponse = testRestTemplate.exchange(
                baseUrl + createUserApi, HttpMethod.POST, createUserRequest, UserDto.class
        );

        Assertions.assertNotNull(createUserResponse);
        Assertions.assertEquals(HttpStatus.CREATED, createUserResponse.getStatusCode());
        Assertions.assertNotNull(createUserResponse.getBody());

        return createUserResponse.getBody();
    }

    private CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        HttpEntity<NewCategoryDto> createCategoryRequest = new HttpEntity<>(newCategoryDto, headers);

        ResponseEntity<CategoryDto> createCategoryResponse = testRestTemplate.exchange(
                baseUrl + createCategoryApi, HttpMethod.POST, createCategoryRequest, CategoryDto.class
        );

        Assertions.assertNotNull(createCategoryResponse);
        Assertions.assertEquals(HttpStatus.CREATED, createCategoryResponse.getStatusCode());
        Assertions.assertNotNull(createCategoryResponse.getBody());

        return createCategoryResponse.getBody();
    }

    private EventFullDto createEvent(NewEventDto newEventDto, long userId) {
        HttpEntity<NewEventDto> createEventRequest = new HttpEntity<>(newEventDto, headers);
        ResponseEntity<EventFullDto> createEventResponse = testRestTemplate.exchange(
                baseUrl + createEventApi(userId), HttpMethod.POST, createEventRequest, EventFullDto.class
        );

        Assertions.assertNotNull(createEventResponse);
        Assertions.assertEquals(HttpStatus.CREATED, createEventResponse.getStatusCode());
        Assertions.assertNotNull(createEventResponse.getBody());

        return createEventResponse.getBody();
    }

    private CommentDto createComment(NewCommentDto newCommentDto, long userId, long eventId) {
        HttpEntity<NewCommentDto> createCommentRequest = new HttpEntity<>(newCommentDto, headers);
        ResponseEntity<CommentDto> createCommentResponse = testRestTemplate.exchange(
                baseUrl + createCommentApi(userId, eventId), HttpMethod.POST,
                createCommentRequest, CommentDto.class
        );

        Assertions.assertNotNull(createCommentResponse);
        Assertions.assertEquals(HttpStatus.CREATED, createCommentResponse.getStatusCode());
        Assertions.assertNotNull(createCommentResponse.getBody());
        return createCommentResponse.getBody();
    }


}
