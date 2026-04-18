package dev.awd.tab5abackend.repository;

import dev.awd.tab5abackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByTitle(String title);
    Optional<Ingredient> findOneByTitle(String title);
}
