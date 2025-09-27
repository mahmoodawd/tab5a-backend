package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlMapper.class})
public interface IngredientMapper {

    @Mapping(target = "imageUrl", source = "imagePath")
    IngredientResponseDto ingredientToIngredientResponseDto(Ingredient ingredient);

    //TODO 03: Map binary Images to paths
    @Mapping(target = "imagePath", source = "image")
    @Mapping(target = "id", ignore = true)
    Ingredient ingredientRequestDtoToIngredient(IngredientRequestDto ingredientRequestDto);
}
