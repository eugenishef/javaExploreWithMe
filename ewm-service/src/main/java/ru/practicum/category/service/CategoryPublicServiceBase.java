package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.util.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.util.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryPublicServiceBase implements CategoryPublicService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category =  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(categoryId, Category.class));
        return categoryMapper.categoryToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.stream()
                .map(categoryMapper::categoryToCategoryDto)
                .toList();
    }
}
