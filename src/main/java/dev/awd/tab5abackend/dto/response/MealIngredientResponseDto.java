package dev.awd.tab5abackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealIngredientResponseDto {
    private IngredientResponseDto ingredient;
    private String measure;

}