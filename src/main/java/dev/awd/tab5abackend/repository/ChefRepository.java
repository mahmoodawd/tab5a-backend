package dev.awd.tab5abackend.repository;

import dev.awd.tab5abackend.model.Chef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefRepository extends JpaRepository<Chef, Long> {

    boolean existsByName(String name);
}
