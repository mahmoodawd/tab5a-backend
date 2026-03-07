package dev.awd.tab5abackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponseDto {
    private Long id;
    private String title;
    private String imageUrl;
}