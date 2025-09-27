package dev.awd.tab5abackend.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter

public class CommentResponseDto {
    private Long id;

    private String body;

    private Instant createdAt;

    private MealResponseDto meal;

    private UserResponseDto user;

}