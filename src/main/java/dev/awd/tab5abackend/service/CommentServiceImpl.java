package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.CommentRequestDto;
import dev.awd.tab5abackend.dto.request.CommentUpdateRequestDto;
import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import dev.awd.tab5abackend.exception.CommentNotFoundException;
import dev.awd.tab5abackend.exception.MealNotFoundException;
import dev.awd.tab5abackend.mapper.CommentMapper;
import dev.awd.tab5abackend.model.Comment;
import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.model.User;
import dev.awd.tab5abackend.repository.CommentRepository;
import dev.awd.tab5abackend.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MealRepository mealRepository;
    private final CommentMapper commentMapper;
    private final MealRatingService mealRatingService;


    @Override
    public List<CommentResponseDto> getMealComments(Long mealId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(() -> new MealNotFoundException(mealId));
        return commentRepository.findAllByMeal(meal)
                .stream().map(commentMapper::commentToCommentResponseDto).toList();
    }

    @Override
    public CommentResponseDto getComment(Long commentId) {
        return commentMapper
                .commentToCommentResponseDto(commentRepository.findById(commentId)
                        .orElseThrow(() -> new CommentNotFoundException(commentId)));
    }

    @Override
    public CommentResponseDto addNewComment(CommentRequestDto commentRequest, Long mealId, User user) {

        Comment toSave = commentMapper.commentRequestDtoToComment(commentRequest);
        toSave.setUser(user);
        toSave.setMeal(mealRepository.findById(mealId).orElseThrow(() -> new MealNotFoundException(mealId)));
        Comment saved = commentRepository.save(toSave);
        mealRatingService.onMealRatingAdded(toSave.getMeal(), toSave.getRating());
        return commentMapper.commentToCommentResponseDto(saved);
    }

    @Override
    public CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto commentRequest) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        BigDecimal oldRating = comment.getRating();
        comment.setBody(commentRequest.getBody());
        comment.setRating(commentRequest.getRating());
        Comment savedComment = commentRepository.save(comment);
        mealRatingService.onMealRatingUpdated(savedComment.getMeal(), savedComment.getRating(), oldRating);
        return commentMapper.commentToCommentResponseDto(savedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        commentRepository.deleteById(commentId);
        mealRatingService.onMealRatingDeleted(comment.getMeal(), comment.getRating());
    }
}
