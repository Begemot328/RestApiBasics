package com.epam.esm.model.entity;


/**
 * Tag entity class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class Tag extends Entity {
    private String name;

    /**
     * Default constructor
     */
    public Tag() {
    }

    /**
     * Constructor
     *
     * @param name name of the tag
     */
    public Tag(String name) {
        this.name = name;
    }


    /**
     * Name getter
     *
     * @return name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     *
     * @param name name of the tag
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return getName() != null ? getName().equals(tag.getName()) : tag.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
