package com.epam.esm.model.entity;

/**
 * Entity class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class Entity {

    /** id of the object */
    protected int id;

    /**
     * Id getter
     *
     * @return id of the entity
     */
    public int getId() {
        return id;
    }

    /**
     * Id setter
     *
     * @param  id id - of the entity
     */
    public void setId(int id) {
        this.id = id;
    }
}
