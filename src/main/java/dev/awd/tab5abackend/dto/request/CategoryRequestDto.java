package dev.awd.tab5abackend.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    private String title;
    private String description;
    private MultipartFile image;
}
