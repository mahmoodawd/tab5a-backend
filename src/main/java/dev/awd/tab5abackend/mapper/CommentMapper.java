package dev.awd.tab5abackend.mapper;

import dev.awd.tab5abackend.dto.request.CommentRequestDto;
import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import dev.awd.tab5abackend.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImageUrlMapper.class})
public interface CommentMapper {

    CommentResponseDto commentToCommentResponseDto(Comment comment);

    //TODO 01: Introduce EntityResolver to ap ids to entities
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "meal", source = "mealId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto);
}
