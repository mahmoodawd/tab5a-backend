package dev.awd.tab5abackend.exception;

public class MealCreationException extends ResourceAlreadyExistException {
    public MealCreationException(String title) {
        super("Failed to create meal: " + title);
    }
}
