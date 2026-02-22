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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MealRepository mealRepository;
    private final CommentMapper commentMapper;
    private final MealRatingService mealRatingService;


    @Override
    public List<CommentResponseDto> getMealComments(Long mealId) {
        log.info("Fetching Meal Comments: {}", mealId);

        Meal meal = mealRepository.findById(mealId).
                orElseThrow(() -> {
                    log.warn("Meal not fount with id: {}", mealId);
                    return new MealNotFoundException(mealId);
                });

        List<CommentResponseDto> mealComments = commentRepository.findAllByMeal(meal)
                .stream().map(commentMapper::commentToCommentResponseDto).toList();

        log.info("Retrieved {} comments", mealComments.size());
        return mealComments;
    }

    @Override
    public CommentResponseDto getComment(Long commentId) {
        log.debug("Fetching comment with id: {}", commentId);

        return commentMapper
                .commentToCommentResponseDto(commentRepository.findById(commentId)
                        .orElseThrow(() -> {
                            log.warn("comment not found with id: {}", commentId);
                            return new CommentNotFoundException(commentId);
                        }));
    }

    @Override
    public CommentResponseDto addNewComment(CommentRequestDto commentRequest, Long mealId, User user) {
        String shortBody = commentRequest.getBody().substring(0, Math.min(20, commentRequest.getBody().length()));
        log.info("Adding new comment: '{}', to meal: {}", shortBody, mealId);

        log.debug("Mapping comment DTO to entity");
        Comment toSave = commentMapper.commentRequestDtoToComment(commentRequest);

        log.debug("Populating comment user and meal");
        toSave.setUser(user);
        toSave.setMeal(mealRepository.findById(mealId).orElseThrow(() -> new MealNotFoundException(mealId)));
        log.debug("Saving comment to database");
        Comment savedComment = commentRepository.save(toSave);

        log.info("Comment created successfully: id={}, body='{}', rating={}",
                savedComment.getId(), shortBody, commentRequest.getRating());
        if (!toSave.getRating().equals(BigDecimal.ZERO)) {
            log.debug("Updating meal rating: {}", mealId);
            mealRatingService.onMealRatingAdded(toSave.getMeal(), toSave.getRating());
        }

        log.debug("Mapping comment entity to response DTO");
        return commentMapper.commentToCommentResponseDto(savedComment);
    }

    @Override
    public CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto commentRequest) {
        log.info("Update comment with id={}", commentId);

        // Fetch and validate comment exists
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.warn("Comment update failed - comment not found. commentId={}", commentId);
                    return new CommentNotFoundException(commentId);
                });

        Long mealId = comment.getMeal().getId();
        BigDecimal oldRating = comment.getRating();
        BigDecimal newRating = commentRequest.getRating();

        log.debug("Updating comment. commentId={}, mealId={}, oldRating={}, newRating={}",
                commentId, mealId, oldRating, newRating);

        // Update comment
        comment.setBody(commentRequest.getBody());
        comment.setRating(newRating);

        Comment savedComment = commentRepository.save(comment);

        log.info("Comment updated successfully. commentId={}, mealId={}, ratingChanged={}",
                commentId, mealId, !Objects.equals(oldRating, newRating));

        // Update meal rating if rating changed
        if (!Objects.equals(oldRating, newRating)) {
            log.debug("Updating meal rating. mealId={}, oldRating={}, newRating={}",
                    mealId, oldRating, newRating);
            mealRatingService.onMealRatingUpdated(
                    savedComment.getMeal(),
                    newRating,
                    oldRating
            );
        }

        log.debug("Mapping comment entity to response DTO");
        return commentMapper.commentToCommentResponseDto(savedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        log.info("Deleting comment: {}", commentId);
        Comment comment = commentRepository
                .findById(commentId).orElseThrow(() -> {
                    log.warn("comment not found with id: {}", commentId);
                    return new CommentNotFoundException(commentId);
                });

        commentRepository.deleteById(commentId);
        log.info("Comment deleted successfully: id={}", commentId);

        if (!comment.getRating().equals(BigDecimal.ZERO)) {
            log.debug("Updating meal rating: {}", comment.getMeal().getId());
            mealRatingService.onMealRatingDeleted(comment.getMeal(), comment.getRating());
        }
    }
}
