package dev.awd.tab5abackend.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserFavoriteRequestDto {
    private String mealId;
    private UUID userId;
}