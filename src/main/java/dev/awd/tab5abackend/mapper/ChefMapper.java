package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.ChefRequestDto;
import dev.awd.tab5abackend.dto.response.ChefResponseDto;
import dev.awd.tab5abackend.model.Chef;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImageUrlMapper.class})
public interface ChefMapper {

    ChefResponseDto chefToChefResponseDto(Chef chef);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Chef chefRequestDtoToChef(ChefRequestDto chefRequestDto);
}
