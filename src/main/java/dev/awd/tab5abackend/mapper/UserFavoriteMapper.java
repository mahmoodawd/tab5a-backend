package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.UserFavoriteRequestDto;
import dev.awd.tab5abackend.dto.response.UserFavoriteResponseDto;
import dev.awd.tab5abackend.mapper.resolver.*;
import dev.awd.tab5abackend.model.UserFavorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlResolver.class, MultipartFileResolver.class, UserResolver.class,
                MealResolver.class, ImageUrlResolver.class, CommentsResolver.class, IngredientsResolver.class})
public interface UserFavoriteMapper {

    @Mapping(target = "id", source = "id.mealId")
    @Mapping(target = "meal.imageUrl", source = "meal.imagePath", qualifiedBy = ImageMapping.class)
    @Mapping(target = "meal.comments", source = "meal.id")
    @Mapping(target = "meal.ingredients", source = "meal.id")
    @Mapping(target = "meal.category.imageUrl", source = "meal.category.imagePath", qualifiedBy = ImageMapping.class)
    UserFavoriteResponseDto userFavoriteToUserFavoriteResponseDto(UserFavorite userFavorite);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "meal", source = "mealId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserFavorite userFavoriteRequestDtoToUserFavorite(UserFavoriteRequestDto userFavoriteRequestDto);
}
