package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.CategoryRequestDto;
import dev.awd.tab5abackend.dto.response.CategoryResponseDto;
import dev.awd.tab5abackend.exception.CategoryAlreadyExistException;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();

    CategoryResponseDto findById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryRequest) throws CategoryAlreadyExistException;
}
