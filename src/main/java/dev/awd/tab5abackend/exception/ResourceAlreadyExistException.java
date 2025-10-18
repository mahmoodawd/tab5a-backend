package dev.awd.tab5abackend.exception;

public abstract class ResourceAlreadyExistException extends Exception {
    protected ResourceAlreadyExistException(String message) {
        super(message);
    }

}
