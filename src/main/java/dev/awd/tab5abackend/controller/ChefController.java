package dev.awd.tab5abackend.controller;

import dev.awd.tab5abackend.dto.request.ChefRequestDto;
import dev.awd.tab5abackend.dto.response.ChefResponseDto;
import dev.awd.tab5abackend.exception.ChefAlreadyExistException;
import dev.awd.tab5abackend.service.ChefService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController()
public class ChefController {

    private final ChefService chefService;

    @GetMapping("/chefs")
    public ResponseEntity<List<ChefResponseDto>> chefs() {
        return ResponseEntity.ok(chefService.findAll());
    }

    @GetMapping("chefs/{id}")
    public ResponseEntity<ChefResponseDto> chef(@PathVariable Long id) {
        return ResponseEntity.ok(chefService.findById(id));
    }

    @PutMapping(value = "/chefs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> newChef(@ModelAttribute ChefRequestDto requestDto) throws ChefAlreadyExistException {
        ChefResponseDto savedChef = chefService.save(requestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedChef.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
