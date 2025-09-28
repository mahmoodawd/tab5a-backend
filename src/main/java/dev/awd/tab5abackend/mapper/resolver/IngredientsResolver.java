package dev.awd.tab5abackend.mapper.resolver;

import dev.awd.tab5abackend.dto.response.MealIngredientResponseDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class IngredientsResolver {
    public List<MealIngredientResponseDto> ofMealId(Long id) {
        return Collections.emptyList();
    }

}
