package dev.awd.tab5abackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "chefs")
public class Chef {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chefs_id_gen")
    @SequenceGenerator(name = "chefs_id_gen", sequenceName = "chefs_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar")
    private String avatar;

    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

}