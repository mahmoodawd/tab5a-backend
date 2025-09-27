package dev.awd.tab5abackend.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IngredientResponseDto {
    private Long id;
    private String title;
    private String imageUrl;
}