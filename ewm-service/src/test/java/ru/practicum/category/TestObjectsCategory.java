package ru.practicum.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

public class TestObjectsCategory {
    public NewCategoryDto newCategoryDto;
    public CategoryDto categoryDto;

    public TestObjectsCategory() {
        newCategoryDto = new NewCategoryDto();
        newCategoryDto.setName("Name");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName(newCategoryDto.getName());
    }
}
