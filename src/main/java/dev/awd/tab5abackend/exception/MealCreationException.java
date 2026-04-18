package dev.awd.tab5abackend.exception;

public class MealCreationException extends RuntimeException {
    public MealCreationException(String errorMessage) {
        super(errorMessage);
    }
}
