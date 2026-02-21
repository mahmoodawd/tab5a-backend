package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.exception.MealAlreadyExistException;
import dev.awd.tab5abackend.exception.MealCreationException;

import java.util.List;

public interface MealService {

    List<MealResponseDto> findAll();

    MealResponseDto findById(Long id);

    MealResponseDto save(MealRequestDto mealRequestDto) throws MealAlreadyExistException, MealCreationException;
}
