package ru.practicum.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

public class TestObjectsCategory {
    public NewCategoryDto newCategoryDto;
    public CategoryDto categoryDto;
    public Category category;

    public TestObjectsCategory() {
        newCategoryDto = new NewCategoryDto();
        newCategoryDto.setName("Name");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName(newCategoryDto.getName());

        category = new Category();
        category.setId(categoryDto.getId());
        category.setName(newCategoryDto.getName());
    }
}
