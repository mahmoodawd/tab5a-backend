package dev.awd.tab5abackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class UserFavoriteId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 7109897246332268581L;
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "meal_id", nullable = false)
    private Long mealId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFavoriteId entity = (UserFavoriteId) o;
        return Objects.equals(this.mealId, entity.mealId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, userId);
    }

}