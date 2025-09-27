package dev.awd.tab5abackend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ChefRequestDto {
    private String name;
    private String bio;
    private MultipartFile avatar;
}
