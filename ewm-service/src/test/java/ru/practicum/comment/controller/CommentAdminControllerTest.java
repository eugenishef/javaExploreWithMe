package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.StatsClient;
import ru.practicum.comment.service.CommentAdminService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentAdminController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentAdminControllerTest {

    @MockBean
    private final CommentAdminService commentAdminService;

    @MockBean
    private final StatsClient statsClient;

    private final MockMvc mvc;

    private String deleteAdminUri = "/admin/comments/{commentId}";

    @Test
    void deleteCommentCorrect() throws Exception {
        Mockito.doNothing().when(commentAdminService)
                .deleteComment(Mockito.anyLong());

        mvc.perform(delete(deleteAdminUri, 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCommentNegativeCommentIdBadRequest() throws Exception {
        mvc.perform(delete(deleteAdminUri, -1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }
}
