package dev.awd.tab5abackend.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserFavoriteResponseDto {
    private Long id;

    private MealResponseDto meal;

    private Instant createdAt;

}