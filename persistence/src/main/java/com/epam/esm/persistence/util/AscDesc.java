package com.epam.esm.persistence.util;

import java.util.Locale;

public enum AscDesc {
    ASC, DESC;

    public static AscDesc getValue(String s) {

        try {
            AscDesc.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            switch(s) {
                case "1":
                    return AscDesc.ASC;
                case "2":
                    return AscDesc.DESC;
            }
            return AscDesc.ASC;
        }
        return AscDesc.valueOf(s.toUpperCase());
    }
}
