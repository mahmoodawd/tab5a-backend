package dev.awd.tab5abackend.service;

import dev.awd.tab5abackend.dto.response.CommentResponseDto;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
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

}