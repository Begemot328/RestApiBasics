package com.epam.esm.model.entity;

import java.util.Arrays;
import java.util.Optional;

public enum RoleEnum {
    GUEST(0, "GUEST"),
    ADMIN(1, "ADMIN"),
    USER(2, "USER");

    private int id;
    private String value;
    private String errorMessage = "Role %s= %s not found!";

    RoleEnum(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public RoleEnum getById(int id) {
        Optional<RoleEnum> roleOptional =  Arrays.stream(values())
                .filter(role -> role.id == id).findAny();
        return roleOptional.orElseThrow(
                () -> new IllegalArgumentException(String.format(errorMessage, "id", id)));
    }

    public RoleEnum getByName(String name) {
        Optional<RoleEnum> roleOptional =  Arrays.stream(values())
                .filter(role -> role.value.equals(name)).findAny();
        return roleOptional.orElseThrow(
                () -> new IllegalArgumentException(String.format(errorMessage, "name", name)));
    }
}
