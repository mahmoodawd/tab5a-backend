package dev.awd.tab5abackend.mapper.resolver;

import dev.awd.tab5abackend.exception.CategoryNotFoundException;
import dev.awd.tab5abackend.model.Category;
import dev.awd.tab5abackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryResolver {
    private final CategoryRepository categoryRepository;

    public Category fromId(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
