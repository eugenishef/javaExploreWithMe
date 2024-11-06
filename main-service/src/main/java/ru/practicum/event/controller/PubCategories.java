package ru.practicum.event.controller;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.CategoryDto;
import ru.practicum.event.model.Category;
import ru.practicum.event.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PubCategories {

    final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getPublicCategories(
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Получение всех категорий с from={} и size={}", from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> categories = categoryService.getCategories(from, size);
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Получение категории по ID={}", catId);
        Category category = categoryService.getCategoryById(catId);
        return convertToDto(category);
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
