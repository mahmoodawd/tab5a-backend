package dev.awd.tab5abackend.exception;

public class CategoryAlreadyExistException extends ResourceAlreadyExistException {
    public CategoryAlreadyExistException(String title) {
        super("Category: " + title + " already exist");
    }
}
