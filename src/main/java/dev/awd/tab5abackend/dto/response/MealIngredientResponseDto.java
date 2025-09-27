package dev.awd.tab5abackend.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealIngredientResponseDto {
    private IngredientResponseDto ingredient;
    private String measure;

}