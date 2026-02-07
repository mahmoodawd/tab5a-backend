package dev.awd.tab5abackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {
    private UUID id;
    private String fullName;
    private String email;
    private String mobile;
    private String avatarUrl;
    private Date createdAt;
    private Date updatedAt;
    private String role;
}
