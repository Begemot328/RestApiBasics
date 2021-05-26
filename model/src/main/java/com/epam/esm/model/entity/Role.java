package com.epam.esm.model.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    GUEST(0, "GUEST"),
    ADMIN(1, "ADMIN"),
    USER(2, "USER");

    private int id;
    private String value;

    Role(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Role getById(int id) {
        Optional<Role> roleOptional =  Arrays.stream(values())
                .filter(role -> role.id == id).findAny();
        return roleOptional.orElseThrow(
                () -> new IllegalArgumentException("Role id=" + id + " not found!"));
    }

    public Role getByName(String name) {
        Optional<Role> roleOptional =  Arrays.stream(values())
                .filter(role -> role.value.equals(name)).findAny();
        return roleOptional.orElseThrow(
                () -> new IllegalArgumentException("Role name =" + name + " not found!"));
    }
}
