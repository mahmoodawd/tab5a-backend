package dev.awd.tab5abackend.exception;

public class UserAlreadyExistException extends ResourceAlreadyExistException {
    public UserAlreadyExistException() {
        super("User already exist, use another email or phone");
    }
}
