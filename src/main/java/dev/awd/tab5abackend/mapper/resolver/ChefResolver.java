package dev.awd.tab5abackend.mapper.resolver;

import dev.awd.tab5abackend.exception.ChefNotFoundException;
import dev.awd.tab5abackend.model.Chef;
import dev.awd.tab5abackend.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChefResolver {

    private final ChefRepository chefRepository;

    public Chef fromId(Long id) {
        return chefRepository.findById(id).orElseThrow(() -> new ChefNotFoundException(id));
    }
}
