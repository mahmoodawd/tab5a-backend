package dev.awd.tab5abackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class IngredientNotFoundException extends ResourceNotFoundException {
    public IngredientNotFoundException(Long id) {
        super("Ingredient not found: " + id);
    }
}
