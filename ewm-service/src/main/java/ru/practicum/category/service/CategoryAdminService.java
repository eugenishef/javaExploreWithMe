package ru.practicum.category.service;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

public interface CategoryAdminService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto);

    void deleteCategory(Long categoryId);
}
