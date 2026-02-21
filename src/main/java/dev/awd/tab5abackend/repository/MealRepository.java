package dev.awd.tab5abackend.repository;

import dev.awd.tab5abackend.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
    boolean existsByTitle(String title);
}
