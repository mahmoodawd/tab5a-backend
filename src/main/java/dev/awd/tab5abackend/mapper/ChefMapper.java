package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.ChefRequestDto;
import dev.awd.tab5abackend.dto.response.ChefResponseDto;
import dev.awd.tab5abackend.mapper.resolver.ImageMapping;
import dev.awd.tab5abackend.mapper.resolver.ImageUrlResolver;
import dev.awd.tab5abackend.mapper.resolver.MultipartFileResolver;
import dev.awd.tab5abackend.model.Chef;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImageUrlResolver.class, MultipartFileResolver.class})
public interface ChefMapper {

    @Mapping(target = "avatar", source = "avatar", qualifiedBy = ImageMapping.class)
    ChefResponseDto chefToChefResponseDto(Chef chef);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Chef chefRequestDtoToChef(ChefRequestDto chefRequestDto);
}
