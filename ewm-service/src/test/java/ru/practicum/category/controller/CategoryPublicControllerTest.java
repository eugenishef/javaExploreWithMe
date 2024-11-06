package ru.practicum.category.controller;

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
import ru.practicum.category.service.CategoryPublicService;
import ru.practicum.dto.category.CategoryDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryPublicController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryPublicControllerTest {
    @MockBean
    private final CategoryPublicService categoryPublicService;
    @MockBean
    private final StatsClient statsClient;

    private final MockMvc mvc;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        categoryDto = testObjectsCategory.categoryDto;
    }

    @Test
    void getCategoryByCorrectId() throws Exception {
        long categoryId = 1L;
        Mockito.when(categoryPublicService.getCategoryById(Mockito.anyLong()))
                .thenReturn(categoryDto);

        mvc.perform(get("/categories/{categoryId}", categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @Test
    void getCategoryByNotCorrectIdGetBadRequest() throws Exception {
        long categoryId = -1L;

        mvc.perform(get("/categories/{categoryId}", categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void getCategoriesWithoutParams() throws Exception {
        Mockito.when(categoryPublicService.getCategories(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(categoryDto));

        mvc.perform(get("/categories")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(categoryDto.getName())));
    }

    @Test
    void getCategoriesWithBadFromParamsGetBadRequest() throws Exception {
        int from = -1;

        mvc.perform(get("/categories?from={from}", from)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }
}