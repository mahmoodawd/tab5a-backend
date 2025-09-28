package dev.awd.tab5abackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends NoSuchElementException {
    public CategoryNotFoundException(Long id) {
        super("Category not found: " + id);
    }
}
