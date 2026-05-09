package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.response.MealResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealFacadeImpl implements MealFacade {
    private final MealService mealService;
    private final CommentService commentService;
    private final IngredientService ingredientService;

    @Transactional(readOnly = true)
    @Override
    public MealResponseDto getFullMealDetails(Long id) {
        log.info("Getting full meal details: {}", id);
        MealResponseDto response = mealService.findById(id);
        log.info("Basic meal retrieved successfully");
        response.setIngredients(ingredientService.getMealIngredients(id));
        log.info("Meal ingredient retrieved successfully");
        response.setComments(commentService.getMealComments(id));
        log.info("Meal comment retrieved successfully");
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MealResponseDto> getAllMealsWithDetails() {
        log.info("Getting all meals details");
        List<MealResponseDto> detailsList = mealService.findAll().stream().peek(
                meal -> {
                    meal.setIngredients(ingredientService.getMealIngredients(meal.getId()));
                    meal.setComments(commentService.getMealComments(meal.getId()));
                }
        ).toList();
        log.info("Meals details retrieved successfully total: {}", detailsList.size());
        return detailsList;
    }
}
