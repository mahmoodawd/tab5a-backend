package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.mapper.resolver.ImageMapping;
import dev.awd.tab5abackend.mapper.resolver.ImageUrlResolver;
import dev.awd.tab5abackend.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlResolver.class})
public interface IngredientMapper {

    @Mapping(target = "imageUrl", source = "imagePath", qualifiedBy = ImageMapping.class)
    IngredientResponseDto ingredientToIngredientResponseDto(Ingredient ingredient);

    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "id", ignore = true)
    Ingredient ingredientRequestDtoToIngredient(IngredientRequestDto ingredientRequestDto);
}
