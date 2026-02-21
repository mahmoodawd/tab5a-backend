package dev.awd.tab5abackend.controller;

import dev.awd.tab5abackend.dto.request.MealRequestDto;
import dev.awd.tab5abackend.dto.response.MealResponseDto;
import dev.awd.tab5abackend.exception.MealAlreadyExistException;
import dev.awd.tab5abackend.exception.MealCreationException;
import dev.awd.tab5abackend.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController()
public class MealController {

    private final MealService mealService;

    @GetMapping("/meals")
    public ResponseEntity<List<MealResponseDto>> getAllMeals() {
        return ResponseEntity.ok(mealService.findAll());
    }

    @GetMapping("meals/{id}")
    public ResponseEntity<MealResponseDto> getMeal(@PathVariable Long id) {
        return ResponseEntity.ok(mealService.findById(id));
    }

    @PostMapping(value = "/meals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> newMeal(@ModelAttribute MealRequestDto requestDto) throws MealAlreadyExistException, MealCreationException {
        MealResponseDto savedMeal = mealService.save(requestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMeal.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
