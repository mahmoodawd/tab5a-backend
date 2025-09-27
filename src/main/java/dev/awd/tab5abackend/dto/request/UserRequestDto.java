package dev.awd.tab5abackend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UserRequestDto {
    private String name;
    private String email;
    private String mobile;
    private MultipartFile avatar;
}