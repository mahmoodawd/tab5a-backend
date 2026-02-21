package dev.awd.tab5abackend.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
public class MealResponseDto {
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private String videoUrl;

    private int ratingCount;

    private BigDecimal ratingAvg;

    private Instant addedAt;

    private CategoryResponseDto category;

    private ChefResponseDto chef;

    private List<MealIngredientResponseDto> ingredients;

    private List<CommentResponseDto> comments;

}