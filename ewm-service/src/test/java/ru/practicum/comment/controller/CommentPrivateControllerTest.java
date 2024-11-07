package ru.practicum.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.StatsClient;
import ru.practicum.category.TestObjectsCategory;
import ru.practicum.comment.TestObjectsComment;
import ru.practicum.comment.service.CommentPrivateService;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.user.TestObjectsUser;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentPrivateController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentPrivateControllerTest {

    @MockBean
    private final CommentPrivateService commentPrivateService;

    @MockBean
    private final StatsClient statsClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    private NewCommentDto newCommentDto;
    private CommentDto commentDto;
    private NewCommentDto updateCommentDto;

    private final String createUriString = "/users/{userId}/events/{eventId}/comments";
    private final String updateDeleteUriString = "/users/{userId}/events/{eventId}/comments/{commentId}";

    @BeforeEach
    void setUp() {
        TestObjectsUser testObjectsUser = new TestObjectsUser();
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        TestObjectsEvent testObjectsEvent = new TestObjectsEvent(testObjectsCategory);
        TestObjectsComment testObjectsComment = new TestObjectsComment(testObjectsUser, testObjectsEvent);

        newCommentDto = testObjectsComment.newCommentDto;
        updateCommentDto = testObjectsComment.updateCommentDto;
        commentDto = testObjectsComment.commentDto;
    }

    @Test
    void createCommentCorrect() throws Exception {
        Mockito.when(commentPrivateService.createComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(commentDto);

        mvc.perform(post(createUriString, 1L, 1L)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }

    @Test
    void createCommentNoTextBadRequest() throws Exception {
        newCommentDto.setText(null);
        mvc.perform(post(createUriString, 1L, 1L)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void createCommentBlankTextBadRequest() throws Exception {
        newCommentDto.setText("   ");
        mvc.perform(post(createUriString, 1L, 1L)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void createCommentNegativeUserIdBadRequest() throws Exception {
        mvc.perform(post(createUriString, -1L, 1L)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void createCommentNegativeEventIdBadRequest() throws Exception {
        mvc.perform(post(createUriString, 1L, -1L)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void updateCommentCorrect() throws Exception {
        commentDto.setText(updateCommentDto.getText());
        Mockito.when(commentPrivateService
                        .updateComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(commentDto);

        mvc.perform(patch(updateDeleteUriString, 1L, 1L, 1L)
                        .content(mapper.writeValueAsString(updateCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }

    @Test
    void updateCommentNoTextBadRequest() throws Exception {
        updateCommentDto.setText(null);
        mvc.perform(patch(updateDeleteUriString, 1L, 1L, 1L)
                        .content(mapper.writeValueAsString(updateCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void updateCommentBlankTextBadRequest() throws Exception {
        updateCommentDto.setText("   ");
        mvc.perform(patch(updateDeleteUriString, 1L, 1L, 1L)
                        .content(mapper.writeValueAsString(updateCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void updateCommentNegativeUserIdBadRequest() throws Exception {
        mvc.perform(patch(updateDeleteUriString, -1L, 1L, 1L)
                        .content(mapper.writeValueAsString(updateCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void updateCommentNegativeEventIdBadRequest() throws Exception {
        mvc.perform(patch(updateDeleteUriString, 1L, -1L, 1L)
                        .content(mapper.writeValueAsString(updateCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void updateCommentNegativeCommentIdBadRequest() throws Exception {
        mvc.perform(patch(updateDeleteUriString, 1L, 1L, -1L)
                        .content(mapper.writeValueAsString(updateCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void deleteCommentCorrect() throws Exception {
        Mockito.doNothing().when(commentPrivateService)
                .deleteComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());

        mvc.perform(delete(updateDeleteUriString, 1L, 1L, 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCommentNegativeUserIdBadRequest() throws Exception {
        mvc.perform(delete(updateDeleteUriString, -1L, 1L, 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void deleteCommentNegativeEventIdBadRequest() throws Exception {
        mvc.perform(delete(updateDeleteUriString, 1L, -1L, 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void deleteCommentNegativeCommentIdBadRequest() throws Exception {
        mvc.perform(delete(updateDeleteUriString, 1L, 1L, -1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }
}