package dev.awd.tab5abackend.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class CategoryRequestDto {
    private String title;
    private String description;
    private MultipartFile image;
}
