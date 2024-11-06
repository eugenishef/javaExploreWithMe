package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.util.mapper.CategoryMapper;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;
import ru.practicum.util.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryAdminServiceBase implements CategoryAdminService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.newCategoryToCategory(newCategoryDto);
        category = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto) {
        Category category = findCategoryByIdOrThrowNotFoundException(categoryId);
        categoryMapper.updateCategoryFromNewCategory(newCategoryDto, category);
        category = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        findCategoryByIdOrThrowNotFoundException(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private Category findCategoryByIdOrThrowNotFoundException(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(categoryId, Category.class));
    }
}
