package com.epam.esm.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Tag entity class.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Entity
public class Tag extends CustomEntity {

    @Column(nullable = false)
    private String name;

    /**
     * Default constructor.
     */
    public Tag() {
    }

    /**
     * Constructor.
     *
     * @param name Name of the tag.
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Name getter.
     *
     * @return Name of the tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     *
     * @param name Name of the tag.
     */
    public void setName(String name) {
        this.name = name;
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
        Tag tag = (Tag) o;
        return getName() != null ? getName().equals(tag.getName()) : tag.getName() == null;
    }

    /**
     * Hach code calculator.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    /**
     * String value generator.
     *
     * @return {@link String} interpretation of object.
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
