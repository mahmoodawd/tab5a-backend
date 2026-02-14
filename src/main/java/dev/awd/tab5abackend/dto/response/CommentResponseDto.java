package dev.awd.tab5abackend.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter

public class CommentResponseDto {
    private Long id;

    private String body;

    private BigDecimal rating;

    private Instant createdAt;

    private CommentAuthorResponseDto author;

}