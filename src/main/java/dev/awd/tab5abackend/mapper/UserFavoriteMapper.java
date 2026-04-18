package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.response.UserFavoriteResponseDto;
import dev.awd.tab5abackend.model.UserFavorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {MealMapper.class})
public interface UserFavoriteMapper {

    @Mapping(target = "id", ignore = true)
    UserFavoriteResponseDto userFavoriteToUserFavoriteResponseDto(UserFavorite userFavorite);
}
