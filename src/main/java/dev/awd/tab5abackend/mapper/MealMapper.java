package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlMapper.class})
public interface MealMapper {

    //TODO 02: Map Ingredients & Comments
    @Mapping(target = "ingredients", source = "title")
    @Mapping(target = "imageUrl", source = "imagePath")
    @Mapping(target = "comments", source = "title")
    MealResponseDto mealToMealResponseDto(Meal meal);

    @Mapping(target = "imagePath", source = "image")
    @Mapping(target = "chef", source = "categoryId")
    @Mapping(target = "category", source = "chefId")
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "ratingAvg", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    Meal mealRequestDtoToMeal(MealRequestDto mealRequestDto);
}
