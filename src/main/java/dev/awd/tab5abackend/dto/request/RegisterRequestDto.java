package dev.awd.tab5abackend.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RegisterRequestDto {
    private String name;
    private String email;
    private String mobile;
    //    private MultipartFile avatar;
    private String password;
}