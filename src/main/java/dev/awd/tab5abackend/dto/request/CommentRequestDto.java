package dev.awd.tab5abackend.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String body;
    private String mealId;
    private String userId;
}