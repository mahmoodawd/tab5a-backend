package dev.awd.tab5abackend.repository;

import dev.awd.tab5abackend.model.Comment;
import dev.awd.tab5abackend.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMeal(Meal meal);
}
