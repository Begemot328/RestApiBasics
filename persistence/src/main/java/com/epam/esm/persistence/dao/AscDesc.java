package com.epam.esm.persistence.dao;

public enum AscDesc {
    ASC, DESC;

    public static AscDesc getValue(String s) {
        try {
            AscDesc.valueOf(s);
        } catch (IllegalArgumentException e) {
            return AscDesc.ASC;
        }
        return AscDesc.valueOf(s);
    }
}
