package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.exception.IngredientAlreadyExistException;

import java.util.List;

public interface IngredientService {
    List<IngredientResponseDto> findAll();

    IngredientResponseDto findById(Long id);

    IngredientResponseDto save(IngredientRequestDto ingredientRequest) throws IngredientAlreadyExistException;
}
