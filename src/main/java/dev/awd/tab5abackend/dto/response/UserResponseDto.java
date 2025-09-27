package dev.awd.tab5abackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class UserResponseDto {
    private UUID id;

    private String name;

    private String email;

    private String mobile;

    private String avatar;

    private Instant createdAt;

    private Instant updatedAt;
}