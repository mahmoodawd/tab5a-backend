package dev.awd.tab5abackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private Instant createdAt;

}