package dev.awd.tab5abackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CommentAuthorResponseDto {
    private String fullName;
    private String email;
    private String avatarUrl;
}
