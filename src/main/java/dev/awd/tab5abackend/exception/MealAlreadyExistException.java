package dev.awd.tab5abackend.exception;

public class MealAlreadyExistException extends ResourceAlreadyExistException {
    public MealAlreadyExistException(String title) {
        super("Meal: " + title + " already exist");
    }
}
