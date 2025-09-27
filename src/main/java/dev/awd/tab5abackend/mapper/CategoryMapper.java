package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.CategoryRequestDto;
import dev.awd.tab5abackend.dto.response.CategoryResponseDto;
import dev.awd.tab5abackend.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlMapper.class})
public interface CategoryMapper {

    @Mapping(target = "imageUrl", source = "imagePath")
    CategoryResponseDto CategoryToCategoryResponseDto(Category category);

    @Mapping(target = "imagePath", source = "image")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Category categoryRequestDtoToCategory(CategoryRequestDto categoryRequestDto);
}
