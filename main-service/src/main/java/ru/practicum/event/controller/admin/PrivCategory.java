package ru.practicum.event.controller.admin;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.CategoryDto;
import ru.practicum.event.model.Category;
import ru.practicum.event.service.CategoryService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivCategory {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(createErrorResponse(categoryDto, "Имя категории не может быть пустым"));
        }

        Category createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdCategory));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto updatedCategoryDto) {
        Category updatedCategory = categoryService.updateCategory(id, updatedCategoryDto);
        return ResponseEntity.ok(convertToDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    private Map<String, Object> createErrorResponse(CategoryDto categoryDto, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("requestBody", categoryDto);
        return errorResponse;
    }
}
