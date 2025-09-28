package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.CommentRequestDto;
import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import dev.awd.tab5abackend.mapper.resolver.*;
import dev.awd.tab5abackend.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageUrlResolver.class, MultipartFileResolver.class, UserResolver.class, MealResolver.class,
                IngredientsResolver.class, CommentsResolver.class})
public interface CommentMapper {

    @Mapping(target = "meal.category.imageUrl", source = "meal.category.imagePath", qualifiedBy = ImageMapping.class)
    @Mapping(target = "meal.imageUrl", source = "meal.imagePath", qualifiedBy = ImageMapping.class)
    @Mapping(target = "meal.comments", source = "meal.id")
    @Mapping(target = "meal.ingredients", source = "meal.id")
    CommentResponseDto commentToCommentResponseDto(Comment comment);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "meal", source = "mealId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto);
}
