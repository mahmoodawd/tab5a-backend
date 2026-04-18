package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.response.MealIngredientResponseDto;
import dev.awd.tab5abackend.mapper.resolver.ImageUrlResolver;
import dev.awd.tab5abackend.model.MealIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlResolver.class, IngredientMapper.class})
public interface MealIngredientMapper {

    MealIngredientResponseDto toResponseDto(MealIngredient mealIngredient);

}
