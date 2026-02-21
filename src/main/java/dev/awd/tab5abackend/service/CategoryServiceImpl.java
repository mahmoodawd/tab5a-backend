package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.ImageType;
import dev.awd.tab5abackend.dto.request.CategoryRequestDto;
import dev.awd.tab5abackend.dto.response.CategoryResponseDto;
import dev.awd.tab5abackend.exception.CategoryAlreadyExistException;
import dev.awd.tab5abackend.exception.CategoryNotFoundException;
import dev.awd.tab5abackend.mapper.CategoryMapper;
import dev.awd.tab5abackend.model.Category;
import dev.awd.tab5abackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UploadService uploadService;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll() {
        log.debug("Fetching all categories");

        List<CategoryResponseDto> categories = categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::CategoryToCategoryResponseDto)
                .toList();

        log.info("Retrieved {} categories", categories.size());
        return categories;
    }

    @Override
    public CategoryResponseDto findById(Long id) {
        log.info("Fetching category with id: {}", id);

        return categoryRepository.findById(id)
                .map(categoryMapper::CategoryToCategoryResponseDto)
                .orElseThrow(() -> {
                    log.warn("Category not found with id: {}", id);
                    return new CategoryNotFoundException(id);
                });
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryRequest) throws CategoryAlreadyExistException {
        String title = categoryRequest.getTitle();

        log.info("Creating new category: '{}'", title);

        if (categoryRepository.existsByTitle(title)) {
            log.warn("Category creation failed - duplicate title: '{}'", title);
            throw new CategoryAlreadyExistException(title);
        }

        log.debug("Mapping category DTO to entity");
        Category categoryToSave = categoryMapper.categoryRequestDtoToCategory(categoryRequest);

        log.debug("Uploading category image");
        String imageStoragePath = uploadService.uploadImage(
                categoryRequest.getImage(),
                ImageType.CATEGORY
        );
        categoryToSave.setImagePath(imageStoragePath);

        log.debug("Saving category to database");
        Category savedCategory = categoryRepository.save(categoryToSave);

        log.info("Category created successfully: id={}, title='{}', imagePath={}",
                savedCategory.getId(), title, imageStoragePath);

        log.debug("Mapping category entity to response DTO");
        return categoryMapper.CategoryToCategoryResponseDto(savedCategory);
    }
}