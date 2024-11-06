package ru.practicum.event.service;

import ru.practicum.event.dto.CategoryDto;
import ru.practicum.event.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getCategories(int from, int size);

    Category getCategoryById(Long id);

    Category createCategory(CategoryDto categoryDto);

    Category updateCategory(Long id, CategoryDto categoryUpdates);

    void deleteCategory(Long id);
}

