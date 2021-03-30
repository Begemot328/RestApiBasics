package com.epam.esm.services.util;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Optional;

public class DoubleUtil {

    public static Optional<BigDecimal> parseDoubleOptional(String s) {
        return Optional.of(parseBigDecimal(s));
    }

    @Nullable
    public static BigDecimal parseBigDecimal(String s) {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
