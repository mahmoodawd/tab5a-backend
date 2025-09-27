package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.UserFavoriteRequestDto;
import dev.awd.tab5abackend.dto.response.UserFavoriteResponseDto;
import dev.awd.tab5abackend.model.UserFavorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlMapper.class})
public interface UserFavoriteMapper {

    UserFavoriteResponseDto userFavoriteToUserFavoriteResponseDto(UserFavorite userFavorite);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "meal", source = "mealId")
    @Mapping(target = "id", ignore = true)
    UserFavorite userFavoriteRequestDtoToUserFavorite(UserFavoriteRequestDto userFavoriteRequestDto);
}
