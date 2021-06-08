package com.epam.esm.model.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Entity class.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@MappedSuperclass
public class CustomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Id getter
     *
     * @return ID of the entity.
     */
    public int getId() {
        return id;
    }

    /**
     * Id setter
     *
     * @param id ID of the entity.
     */
    public void setId(int id) {
        this.id = id;
    }
}
