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


    @Mapping(target = "author.fullName", source = "user.name")
    @Mapping(target = "author.avatarUrl", source = "user.avatar", qualifiedBy = ImageMapping.class)
    CommentResponseDto commentToCommentResponseDto(Comment comment);


    @Mapping(target = "user", ignore = true)
    @Mapping(target = "meal", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto);
}
