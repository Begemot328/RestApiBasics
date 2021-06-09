package com.epam.esm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    /**
     * Name getter.
     *
     * @return Name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     *
     * @param name Name of the role.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Description getter.
     *
     * @return Description of the role.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description setter.
     *
     * @param description Description of the role.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Equality check.
     *
     * @param o Object to check equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id && Objects.equals(name, role.name)
                && Objects.equals(description, role.description);
    }

    /**
     * Hash code calculator.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    /**
     * String value generator.
     *
     * @return {@link String} interpretation of object.
     */
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", value='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
