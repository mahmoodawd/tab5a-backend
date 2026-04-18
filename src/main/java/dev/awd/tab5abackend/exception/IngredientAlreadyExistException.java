package dev.awd.tab5abackend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IngredientAlreadyExistException extends ResourceAlreadyExistException {
    public IngredientAlreadyExistException(String title) {
        super("Ingredient: " + title + " already Exist");
    }
}
