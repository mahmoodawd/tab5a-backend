package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.mapper.resolver.ImageMapping;
import dev.awd.tab5abackend.mapper.resolver.ImageUrlResolver;
import dev.awd.tab5abackend.mapper.resolver.MultipartFileResolver;
import dev.awd.tab5abackend.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlResolver.class, MultipartFileResolver.class, CategoryMapper.class, ChefMapper.class})
public interface MealMapper {

    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "imageUrl", source = "imagePath", qualifiedBy = ImageMapping.class)
    MealResponseDto mealToMealResponseDto(Meal meal);

    @Mapping(target = "imagePath", source = "image")
    @Mapping(target = "ratingSum", ignore = true)
    @Mapping(target = "chef", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "ratingAvg", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    Meal mealRequestDtoToMeal(MealRequestDto mealRequestDto);
}
