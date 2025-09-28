package dev.awd.tab5abackend.mapper.resolver;

import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CommentsResolver {
    public List<CommentResponseDto> ofMealId(Long id) {
        return Collections.emptyList();
    }
}
