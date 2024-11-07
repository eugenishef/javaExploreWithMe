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
import ru.practicum.comment.service.CommentPublicService;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.event.TestObjectsEvent;
import ru.practicum.user.TestObjectsUser;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentPublicController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentPublicControllerTest {

    @MockBean
    private final CommentPublicService commentPublicService;

    @MockBean
    private final StatsClient statsClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    private NewCommentDto newCommentDto;
    private CommentDto commentDto;
    private NewCommentDto updateCommentDto;

    private String getUri = "/events/{eventId}/comments";

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
    void correctGetById() throws Exception {
        Mockito.when(commentPublicService.getCommentById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(commentDto);

        mvc.perform(get(getUri + "/{commentId}", 1L, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }

    @Test
    void negativeEventIdGetByIdBadRequest() throws Exception {
        mvc.perform(get(getUri + "/{commentId}", -1L, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void negativeCommentIdGetByIdBadRequest() throws Exception {
        mvc.perform(get(getUri + "/{commentId}", 1L, -1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void correctGetAllByEventIdWithFromSize() throws Exception {
        Mockito.when(commentPublicService.getEventComment(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(commentDto));

        mvc.perform(get(getUri + "?from={from}&size={size}", 1L, 0, 5)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text", is(commentDto.getText())));
    }

    @Test
    void correctGetAllByEventIdWithoutFromSize() throws Exception {
        Mockito.when(commentPublicService.getEventComment(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(commentDto));

        mvc.perform(get(getUri, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text", is(commentDto.getText())));
    }

    @Test
    void negativeEventIdGetAllByEventIdWithoutFromSize() throws Exception {
        mvc.perform(get(getUri, -1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void negativeSizeGetAllByEventIdWithoutFromSize() throws Exception {
        mvc.perform(get(getUri + "?from={from}&size={size}", 1L, 0, -5)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }
}
