package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.request.CommentRequestDto;
import dev.awd.tab5abackend.dto.request.CommentUpdateRequestDto;
import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import dev.awd.tab5abackend.exception.CommentNotFoundException;
import dev.awd.tab5abackend.exception.MealNotFoundException;
import dev.awd.tab5abackend.mapper.CommentMapper;
import dev.awd.tab5abackend.model.Comment;
import dev.awd.tab5abackend.model.Meal;
import dev.awd.tab5abackend.repository.CommentRepository;
import dev.awd.tab5abackend.repository.MealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    MealRepository mealRepository;

    @Mock
    CommentMapper commentMapper;

    @Mock
    MealRatingService mealRatingService;


    @Test
    public void CommentService_GetMealComments_ReturnsCommentList() {
        List<Comment> commentsList = List.of(new Comment());

        when(mealRepository.findById(anyLong())).thenReturn(Optional.of(new Meal()));
        when(commentRepository.findAllByMeal(any(Meal.class))).thenReturn(commentsList);

        List<CommentResponseDto> mealComments = commentService.getMealComments(123L);

        assertNotNull(mealComments);
        assertEquals(mealComments.size(), 1);
    }

    @Test
    public void CommentService_GetMealCommentsNonExistingMeal_ThrowsException() {
        when(mealRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> commentService.getMealComments(1L));
    }

    @Test
    public void CommentService_GetComment_ReturnsCommentResponse() {
        Comment comment = new Comment(1L, "nice meal", BigDecimal.valueOf(4.5), Instant.now(), null, null);
        CommentResponseDto expectedDto = new CommentResponseDto(1L, "nice meal", BigDecimal.valueOf(4.5), Instant.now(), null);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentMapper.commentToCommentResponseDto(any(Comment.class))).thenReturn(expectedDto);

        CommentResponseDto commentResponse = commentService.getComment(1L);

        assertNotNull(commentResponse);
        assertEquals(commentResponse.getBody(), "nice meal");

    }

    @Test
    public void CommentService_CommentWithNonExistingId_ThrowsException() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.getComment(1L));
    }

    @Test
    public void CommentService_AddCommentWithValidInfo_CommentCreated() {
        Comment comment = new Comment(1L, "nice meal", BigDecimal.valueOf(4.5), Instant.now(), null, null);
        CommentRequestDto commentRequest = new CommentRequestDto("nice meal", BigDecimal.valueOf(4.5));
        CommentResponseDto expectedDto = new CommentResponseDto(1L, "nice meal", BigDecimal.valueOf(4.5), Instant.now(), null);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.commentRequestDtoToComment(any(CommentRequestDto.class))).thenReturn(comment);
        when(mealRepository.findById(anyLong())).thenReturn(Optional.of(new Meal()));
        when(commentMapper.commentToCommentResponseDto(any(Comment.class))).thenReturn(expectedDto);

        CommentResponseDto commentResponse = commentService.addNewComment(commentRequest, 1L, null);

        assertNotNull(commentResponse);
        assertEquals(commentResponse.getId(), 1L);
        verify(mealRatingService).onMealRatingAdded(any(Meal.class), any(BigDecimal.class));
    }

    @Test
    public void CommentService_AddingCommentWithZeroRating_CommentCreatedNoRatingChange() {
        Comment comment = new Comment(1L, "nice meal", BigDecimal.ZERO, Instant.now(), null, null);
        CommentRequestDto commentRequest = new CommentRequestDto("nice meal", BigDecimal.ZERO);
        CommentResponseDto expectedDto = new CommentResponseDto(1L, "nice meal", BigDecimal.ZERO, Instant.now(), null);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.commentRequestDtoToComment(any(CommentRequestDto.class))).thenReturn(comment);
        when(mealRepository.findById(anyLong())).thenReturn(Optional.of(new Meal()));
        when(commentMapper.commentToCommentResponseDto(any(Comment.class))).thenReturn(expectedDto);

        CommentResponseDto commentResponse = commentService.addNewComment(commentRequest, 1L, null);

        assertNotNull(commentResponse);
        assertEquals(commentResponse.getId(), 1L);
        verify(mealRatingService, never()).onMealRatingAdded(any(Meal.class), any(BigDecimal.class));
    }

    @Test
    public void CommentService_UpdateExistingComment_CommentUpdated() {
        Meal commentMeal = new Meal();
        commentMeal.setId(1L);
        Comment oldComment = new Comment(1L, "existing comment", BigDecimal.valueOf(3.5), Instant.now(), commentMeal, null);
        Comment newComment = new Comment(1L, "updated comment", BigDecimal.valueOf(4.5), Instant.now(), commentMeal, null);
        CommentUpdateRequestDto commentRequest = new CommentUpdateRequestDto("nice meal", BigDecimal.valueOf(4.5));
        CommentResponseDto expectedDto = new CommentResponseDto(1L, "nice meal", BigDecimal.valueOf(4.5), Instant.now(), null);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(oldComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);
        when(commentMapper.commentToCommentResponseDto(any(Comment.class))).thenReturn(expectedDto);

        CommentResponseDto commentResponse = commentService.updateComment(1L, commentRequest);

        assertNotNull(commentResponse);
        verify(mealRatingService).onMealRatingUpdated(any(Meal.class), any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    public void CommentService_UpdateExistingCommentWithSameRating_NoRatingUpdate() {
        Meal commentMeal = new Meal();
        commentMeal.setId(1L);
        Comment oldComment = new Comment(1L, "existing comment", BigDecimal.valueOf(4.5), Instant.now(), commentMeal, null);
        Comment newComment = new Comment(1L, "updated comment", BigDecimal.valueOf(4.5), Instant.now(), commentMeal, null);
        CommentUpdateRequestDto commentRequest = new CommentUpdateRequestDto("nice meal", BigDecimal.valueOf(4.5));
        CommentResponseDto expectedDto = new CommentResponseDto(1L, "nice meal", BigDecimal.valueOf(4.5), Instant.now(), null);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(oldComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);
        when(commentMapper.commentToCommentResponseDto(any(Comment.class))).thenReturn(expectedDto);

        CommentResponseDto commentResponse = commentService.updateComment(1L, commentRequest);

        assertNotNull(commentResponse);
        verify(mealRatingService, never()).onMealRatingUpdated(any(Meal.class), any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    public void CommentService_UpdateNonExistingComment_ThrowsException() {
        CommentUpdateRequestDto commentRequest = new CommentUpdateRequestDto("nice meal", BigDecimal.valueOf(4.5));

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(1L, commentRequest));
    }

    @Test
    public void CommentService_DeleteExistingComment_CommentDeletedAndRatingUpdated() {
        Meal commentMeal = new Meal();
        commentMeal.setId(1L);
        Comment comment = new Comment(1L, "existing comment", BigDecimal.valueOf(4.5), Instant.now(), commentMeal, null);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(mealRatingService).onMealRatingDeleted(any(Meal.class), any(BigDecimal.class));
    }

    @Test
    public void CommentService_DeleteZeroRatingExistingComment_CommentDeletedAndNoRatingChange() {
        Meal commentMeal = new Meal();
        commentMeal.setId(1L);
        Comment comment = new Comment(1L, "existing comment", BigDecimal.ZERO, Instant.now(), commentMeal, null);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(mealRatingService, never()).onMealRatingDeleted(any(Meal.class), any(BigDecimal.class));
    }

    @Test
    public void CommentService_DeleteNonExistingComment_ThrowsException() {

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(1L));
    }


}