package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.mapper.resolver.*;
import dev.awd.tab5abackend.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlResolver.class, MultipartFileResolver.class, ChefResolver.class, CategoryResolver.class,
                CommentsResolver.class, IngredientsResolver.class})
public interface MealMapper {

    @Mapping(target = "imageUrl", source = "imagePath", qualifiedBy = ImageMapping.class)
    @Mapping(target = "ingredients", source = "id")
    @Mapping(target = "comments", source = "id")
    @Mapping(target = "category.imageUrl", source = "category.imagePath", qualifiedBy = ImageMapping.class)
    MealResponseDto mealToMealResponseDto(Meal meal);

    @Mapping(target = "imagePath", source = "image")
    @Mapping(target = "chef", source = "chefId")
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "ratingAvg", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    Meal mealRequestDtoToMeal(MealRequestDto mealRequestDto);
}
