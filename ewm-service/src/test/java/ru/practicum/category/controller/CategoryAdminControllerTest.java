package ru.practicum.category.controller;

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
import ru.practicum.category.service.CategoryAdminService;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryAdminController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryAdminControllerTest {
    @MockBean
    private final CategoryAdminService categoryAdminService;
    @MockBean
    private final StatsClient statsClient;

    private final ObjectMapper mapper;
    private final MockMvc mvc;

    private NewCategoryDto newCategoryDto;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        TestObjectsCategory testObjectsCategory = new TestObjectsCategory();
        newCategoryDto = testObjectsCategory.newCategoryDto;
        categoryDto = testObjectsCategory.categoryDto;
    }

    @Test
    void createCorrectCategory() throws Exception {
        Mockito.when(categoryAdminService.createCategory(Mockito.any()))
                .thenReturn(categoryDto);

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @Test
    void createTooLongNameCategoryGetBadRequest() throws Exception {
        newCategoryDto.setName("n".repeat(70));

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void createNullNameCategoryGetBadRequest() throws Exception {
        newCategoryDto.setName(null);

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void updateCorrectCategory() throws Exception {
        long categoryId = 1L;
        Mockito.when(categoryAdminService.updateCategory(Mockito.anyLong(), Mockito.any()))
                .thenReturn(categoryDto);

        mvc.perform(patch("/admin/categories/{categoryId}", categoryId)
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @Test
    void updateNotCorrectIdCategoryGetBadRequest() throws Exception {
        long categoryId = -1L;

        mvc.perform(patch("/admin/categories/{categoryId}", categoryId)
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void updateBlankNameCategoryGetBadRequest() throws Exception {
        long categoryId = 1L;
        newCategoryDto.setName("");

        mvc.perform(patch("/admin/categories/{categoryId}", categoryId)
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }

    @Test
    void deleteCorrectCategoryId() throws Exception {
        long categoryId = 1L;
        Mockito.doNothing().when(categoryAdminService).deleteCategory(Mockito.anyLong());

        mvc.perform(delete("/admin/categories/{categoryId}", categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotCorrectIdCategoryGetBadRequest() throws Exception {
        long categoryId = -1L;

        mvc.perform(delete("/admin/categories/{categoryId}", categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.toString())));
    }
}