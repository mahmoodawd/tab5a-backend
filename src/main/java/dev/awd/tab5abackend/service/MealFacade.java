package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.response.MealResponseDto;

import java.util.List;

public interface MealFacade {
    MealResponseDto getFullMealDetails(Long id);

    List<MealResponseDto> getAllMealsWithDetails();
}
