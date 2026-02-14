package dev.awd.tab5abackend.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CommentUpdateRequestDto {
    private String body;
    private BigDecimal rating;
}