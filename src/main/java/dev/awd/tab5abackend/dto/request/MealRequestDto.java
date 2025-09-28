package dev.awd.tab5abackend.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Setter
@Getter
public class MealRequestDto {
    private String title;
    private String description;
    private MultipartFile image;
    private String videoUrl;
    private Long categoryId;
    private Long chefId;
    private Map<String, String> ingredients;
}