package dev.awd.tab5abackend.repository;

import dev.awd.tab5abackend.model.MealIngredient;
import dev.awd.tab5abackend.model.MealIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealIngredientRepository extends JpaRepository<MealIngredient, MealIngredientId> {
}
