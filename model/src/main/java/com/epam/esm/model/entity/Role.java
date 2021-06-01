package com.epam.esm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Role extends CustomEntity {

    @Column
    private String name;

    @Column
    private String description;

    public Role(String name) {
        this.name = name;
    }

    /**
     * Default constructor.
     */
    public Role() {
        // Default constructor for JPA purposes.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id && Objects.equals(name, role.name)
                && Objects.equals(description, role.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", value='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
