package ru.practicum.event.controller.admin;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.CategoryDto;
import ru.practicum.event.mapper.CategoryMapper;
import ru.practicum.event.model.Category;
import ru.practicum.event.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivCategory {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Создание категории: {}", categoryDto);
        Category createdCategory = categoryService.createCategory(categoryDto);
        return categoryMapper.fromCategoryToDto(createdCategory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto updatedCategoryDto) {
        log.info("Обновление категории с ID {}: {}", id, updatedCategoryDto);
        Category updatedCategory = categoryService.updateCategory(id, updatedCategoryDto);
        return ResponseEntity.ok(categoryMapper.fromCategoryToDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        log.info("Удаление категории с ID {}", id);
        categoryService.deleteCategory(id);
    }
}
