package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.CategoryRequestDto;
import dev.awd.tab5abackend.dto.response.CategoryResponseDto;
import dev.awd.tab5abackend.exception.CategoryAlreadyExistException;
import dev.awd.tab5abackend.exception.CategoryNotFoundException;
import dev.awd.tab5abackend.mapper.CategoryMapper;
import dev.awd.tab5abackend.model.Category;
import dev.awd.tab5abackend.repository.CategoryRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    UploadService uploadService;

    @Test
    void CategoryService_FindAll_ReturnsAllCategories() {
        List<Category> categoryList = List.of(
                new Category(1L, "cat1", "cat1-desc", "images/cat1.png", Instant.now()),
                new Category(2L, "cat2", "cat2-desc", "images/cat2.png", Instant.now())
        );
        when(categoryRepository.findAll()).thenReturn(categoryList);
        List<CategoryResponseDto> allCategories = categoryService.findAll();
        assertNotNull(allCategories);
        assertFalse(allCategories.isEmpty());
        assertEquals(categoryList.size(), 2);
    }

    @Test
    void CategoryService_FindById_ReturnsCategoryResponseDto() {
        Category category1 = new Category(1L, "cat1", "cat1-desc", "images/cat1.png", Instant.now());
        CategoryResponseDto expectedDto = new CategoryResponseDto(1L, "cat1", "cat1-desc", "images/cat1.png", Instant.now());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        when(categoryMapper.CategoryToCategoryResponseDto(category1)).thenReturn(expectedDto);
        CategoryResponseDto responseDto = categoryService.findById(1L);
        assertNotNull(responseDto);
        assertEquals(responseDto.getDescription(), category1.getDescription());
    }

    @Test
    void CategoryService_FindByNonExistingId_ThrowsException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(1L));
    }

    @SneakyThrows
    @Test
    void CategoryService_CreateNewCategoryWithValidInfo_CategoryCreated() {
        Category category1 = new Category(1L, "cat1", "cat1-desc", "images/cat1.png", Instant.now());
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        CategoryRequestDto categoryRequest = new CategoryRequestDto("cat1", "cat1-desc", image);
        CategoryResponseDto expectedDto = new CategoryResponseDto(1L, "cat1", "cat1-desc", "images/cat1.png", Instant.now());

        when(categoryMapper.categoryRequestDtoToCategory(any(CategoryRequestDto.class))).thenReturn(category1);
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);
        when(categoryMapper.CategoryToCategoryResponseDto(any(Category.class))).thenReturn(expectedDto);

        CategoryResponseDto responseDto = categoryService.save(categoryRequest);

        assertNotNull(responseDto);
        assertEquals(responseDto.getId(), 1L);
    }

    @SneakyThrows
    @Test
    void CategoryService_CreateNewCategoryWithExistingTitle_ThrowsException() {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        CategoryRequestDto categoryRequest = new CategoryRequestDto("cat1", "cat1-desc", image);

        when(categoryRepository.existsByTitle(anyString())).thenReturn(true);

        assertThrows(CategoryAlreadyExistException.class, () -> categoryService.save(categoryRequest));

    }

}