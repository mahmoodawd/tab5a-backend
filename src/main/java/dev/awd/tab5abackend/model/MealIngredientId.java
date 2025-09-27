package dev.awd.tab5abackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MealIngredientId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -490845094242126834L;
    @Column(name = "meal_id", nullable = false)
    private Long mealId;

    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MealIngredientId entity = (MealIngredientId) o;
        return Objects.equals(this.ingredientId, entity.ingredientId) &&
                Objects.equals(this.mealId, entity.mealId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, mealId);
    }

}