package dev.awd.tab5abackend.controller;

import dev.awd.tab5abackend.dto.request.IngredientRequestDto;
import dev.awd.tab5abackend.dto.response.IngredientResponseDto;
import dev.awd.tab5abackend.exception.IngredientAlreadyExistException;
import dev.awd.tab5abackend.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController()
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientResponseDto>> ingredients() {
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @GetMapping("ingredients/{id}")
    public ResponseEntity<IngredientResponseDto> ingredient(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.findById(id));
    }

    @PutMapping(value = "/ingredients", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> newIngredient(@ModelAttribute IngredientRequestDto requestDto) throws IngredientAlreadyExistException {
        IngredientResponseDto savedIngredient = ingredientService.save(requestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedIngredient.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
