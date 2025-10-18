package dev.awd.tab5abackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class MealNotFoundException extends ResourceNotFoundException {
    public MealNotFoundException(Long id) {
        super("Meal not found: " + id);
    }
}
