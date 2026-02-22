package dev.awd.tab5abackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;

    private String body;

    private BigDecimal rating;

    private Instant createdAt;

    private CommentAuthorResponseDto author;

}