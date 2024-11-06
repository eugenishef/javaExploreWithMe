package ru.practicum.event.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.CategoryDto;
import ru.practicum.event.exception.NotFoundException;
import ru.practicum.event.mapper.CategoryMapper;
import ru.practicum.event.model.Category;
import ru.practicum.event.repository.CategoryRepository;
import ru.practicum.event.service.CategoryService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper; // Используем маппер для преобразований

    @Override
    public List<Category> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).getContent();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id " + id + " не найдена"));
    }

    @Override
    public Category createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.fromDtoToCategory(categoryDto);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDto categoryUpdates) {
        Category existingCategory = getCategoryById(id);
        if (categoryUpdates.getName() != null) {
            existingCategory.setName(categoryUpdates.getName());
        }
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Категория с id " + id + " не найдена");
        }
        categoryRepository.deleteById(id);
    }
}

