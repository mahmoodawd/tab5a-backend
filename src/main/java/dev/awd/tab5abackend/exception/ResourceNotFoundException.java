package dev.awd.tab5abackend.exception;

import java.util.NoSuchElementException;

public abstract class ResourceNotFoundException extends NoSuchElementException {
    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
