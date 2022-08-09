package com.devsuperior.dscatalog.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tb_category")
public class Category implements Serializable {
    // Serializable -> converter o objeto em bytes para poder trafegar na rede
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate created_At;

    public Category() {
    }

    public Category(Long id, String name, LocalDate created_At) {
        this.id = id;
        this.name = name;
        this.created_At = created_At;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreated_At() {
        return created_At;
    }
    public void setCreated_At(LocalDate created_At) {
        this.created_At = created_At;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
