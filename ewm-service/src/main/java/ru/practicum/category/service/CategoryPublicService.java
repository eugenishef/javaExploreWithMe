package ru.practicum.category.service;

import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryPublicService {
    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getCategories(Integer from, Integer size);
}
