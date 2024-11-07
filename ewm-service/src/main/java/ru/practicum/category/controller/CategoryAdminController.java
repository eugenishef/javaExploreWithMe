package ru.practicum.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.service.CategoryAdminService;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

@RestController
@RequestMapping("/admin/categories")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryAdminController {

    public static final String CATEGORY_ID_PATH = "/{category-id}";

    final CategoryAdminService categoryAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryAdminService.createCategory(newCategoryDto);
    }

    @PatchMapping(CATEGORY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable("category-id") @Positive @NotNull Long categoryId,
                                      @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryAdminService.updateCategory(categoryId, newCategoryDto);
    }

    @DeleteMapping(CATEGORY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("category-id") @Positive @NotNull Long categoryId) {
        categoryAdminService.deleteCategory(categoryId);
    }
}
