package dev.awd.tab5abackend.exception;


public class IngredientAlreadyExistException extends ResourceAlreadyExistException {
    public IngredientAlreadyExistException(String title) {
        super("Ingredient: " + title + " already Exist");
    }
}
