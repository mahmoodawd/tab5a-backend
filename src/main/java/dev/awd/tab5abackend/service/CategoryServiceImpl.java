package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.CategoryRequestDto;
import dev.awd.tab5abackend.dto.response.CategoryResponseDto;
import dev.awd.tab5abackend.exception.CategoryNotFoundException;
import dev.awd.tab5abackend.mapper.CategoryMapper;
import dev.awd.tab5abackend.model.Category;
import dev.awd.tab5abackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll() {
        return
                categoryRepository
                        .findAll()
                        .stream()
                        .map(categoryMapper::CategoryToCategoryResponseDto)
                        .toList();
    }

    @Override
    public CategoryResponseDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::CategoryToCategoryResponseDto)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto category) {
        Category savedCategory = categoryRepository.save(categoryMapper.categoryRequestDtoToCategory(category));
        return categoryMapper.CategoryToCategoryResponseDto(savedCategory);
    }
}
