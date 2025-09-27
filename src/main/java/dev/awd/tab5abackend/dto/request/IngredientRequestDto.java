package dev.awd.tab5abackend.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class IngredientRequestDto {
    private String title;
    private MultipartFile image;
}