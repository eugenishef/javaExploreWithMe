package ru.practicum.category.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.service.CategoryPublicService;
import ru.practicum.constants.paths.ApiPaths;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.PUBLIC_CATEGORIES_BASE_PATH)
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryPublicController {
    final CategoryPublicService categoryPublicService;

    @GetMapping(ApiPaths.CATEGORY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable @Positive @NotNull Long categoryId) {
        return categoryPublicService.getCategoryById(categoryId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(
            @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive Integer size) {
        return categoryPublicService.getCategories(from, size);
    }
}
