package dev.awd.tab5abackend.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class CategoryResponseDto {
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private Instant createdAt;

}