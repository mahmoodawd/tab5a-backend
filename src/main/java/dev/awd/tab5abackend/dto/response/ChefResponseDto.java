package dev.awd.tab5abackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ChefResponseDto {
    private Long id;
    private String name;
    private String bio;
    private String avatar;
    private Instant createdAt;
}
