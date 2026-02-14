package dev.awd.tab5abackend.controller;

import dev.awd.tab5abackend.dto.request.CommentRequestDto;
import dev.awd.tab5abackend.dto.request.CommentUpdateRequestDto;
import dev.awd.tab5abackend.dto.response.CommentResponseDto;
import dev.awd.tab5abackend.model.User;
import dev.awd.tab5abackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/meals/{mealId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    /**
     * Get all comments for a meal
     * GET /api/meals/1/comments
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getMealComments(
            @PathVariable Long mealId) {

        List<CommentResponseDto> comments = commentService.getMealComments(mealId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Add a new comment to a meal
     * POST /api/meals/1/comments
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long mealId,
            @RequestBody CommentRequestDto request,
            @AuthenticationPrincipal User user) {

        CommentResponseDto response = commentService.addNewComment(request, mealId, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Update an existing comment
     * PUT /api/meals/1/comments/5
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @RequestBody CommentUpdateRequestDto request, @PathVariable Long commentId, @PathVariable String mealId) {

        CommentResponseDto response = commentService.updateComment(commentId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete a comment
     * DELETE /api/meals/1/comments/5
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long mealId,
            @PathVariable Long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get a specific comment
     * GET /api/meals/1/comments/5
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(
            @PathVariable Long mealId,
            @PathVariable Long commentId) {

        CommentResponseDto response = commentService.getComment(commentId);
        return ResponseEntity.ok(response);
    }
}