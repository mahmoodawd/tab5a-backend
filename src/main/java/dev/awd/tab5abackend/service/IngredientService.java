package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.request.MealIngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.dto.response.MealIngredientResponseDto;
import dev.awd.tab5abackend.exception.IngredientAlreadyExistException;
import dev.awd.tab5abackend.model.Ingredient;
import dev.awd.tab5abackend.model.Meal;

import java.util.List;

public interface IngredientService {
    List<IngredientResponseDto> findAll();

    IngredientResponseDto findById(Long id);

    IngredientResponseDto save(IngredientRequestDto ingredientRequest) throws IngredientAlreadyExistException;

    Ingredient findEntityByTitle(String title);

    List<MealIngredientResponseDto> getMealIngredients(Long mealId);

    void insertIngredients(Meal meal, List<MealIngredientRequestDto> ingredientsMeasures);
}
