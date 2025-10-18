package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.CategoryRequestDto;
import dev.awd.tab5abackend.dto.response.CategoryResponseDto;
import dev.awd.tab5abackend.mapper.CategoryMapper;
import dev.awd.tab5abackend.model.Category;
import dev.awd.tab5abackend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    private Category category;
    private CategoryResponseDto categoryResponseDto;
    private CategoryRequestDto categoryRequestDto;

    @BeforeEach
    void setup() {
        category = new Category();
        category.setId(1L);
        category.setTitle("Test");
        category.setDescription("Test Description");
        category.setImagePath("path/to/image");

        categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(1L);
        categoryResponseDto.setTitle("Test");
        categoryResponseDto.setDescription("Test Description");
        categoryResponseDto.setImageUrl("localhost/path/to/image");

        categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setTitle("Test");
        categoryRequestDto.setDescription("Test Description");
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake image".getBytes());
        categoryRequestDto.setImage(image);

    }

    @Test
    void CategoryService_FindById_ReturnsCategoryResponseDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.CategoryToCategoryResponseDto(category)).thenReturn(categoryResponseDto);
        CategoryResponseDto responseDto = categoryService.findById(1L);
        assertNotNull(responseDto);
        assertEquals(responseDto.getDescription(), categoryResponseDto.getDescription());
    }
}