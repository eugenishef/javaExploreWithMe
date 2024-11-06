package ru.practicum.event.mapper;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto fromCategoryToDto(Category category);

    Category fromDtoToCategory(CategoryDto categoryDto);

    List<CategoryDto> fromListCategoriesToDto(List<Category> categories);
}
