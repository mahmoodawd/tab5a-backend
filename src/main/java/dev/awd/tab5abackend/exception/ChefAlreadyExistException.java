package dev.awd.tab5abackend.exception;

public class ChefAlreadyExistException extends ResourceAlreadyExistException {
    public ChefAlreadyExistException(String name) {
        super("Chef: " + name + " already exist");
    }
}
