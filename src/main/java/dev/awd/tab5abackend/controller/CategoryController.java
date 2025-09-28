package dev.awd.tab5abackend.controller;

import dev.awd.tab5abackend.dto.request.CategoryRequestDto;
import dev.awd.tab5abackend.dto.response.CategoryResponseDto;
import dev.awd.tab5abackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController()
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> categories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("categories/{id}")
    public ResponseEntity<CategoryResponseDto> category(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PutMapping("/categories")
    public ResponseEntity<Void> newCategory(@RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto savedCategory = categoryService.save(requestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()           // /categories
                .path("/{id}")                  // append /{id}
                .buildAndExpand(savedCategory.getId())  // replace {id} with actual id
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
