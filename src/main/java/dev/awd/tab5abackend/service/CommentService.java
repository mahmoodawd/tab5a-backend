package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.CommentRequestDto;
import dev.awd.tab5abackend.dto.request.CommentUpdateRequestDto;
import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import dev.awd.tab5abackend.model.User;

import java.util.List;

public interface CommentService {
    List<CommentResponseDto> getMealComments(Long mealId);

    CommentResponseDto getComment(Long commentId);

    CommentResponseDto addNewComment(CommentRequestDto commentRequest, Long mealId, User user);

    CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto commentRequest);

    void deleteComment(Long commentId);
}
