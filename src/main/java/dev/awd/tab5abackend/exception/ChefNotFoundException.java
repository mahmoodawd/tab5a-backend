package dev.awd.tab5abackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChefNotFoundException extends ResourceNotFoundException {
    public ChefNotFoundException(Long id) {
        super("Chef not found: " + id);
    }
}
