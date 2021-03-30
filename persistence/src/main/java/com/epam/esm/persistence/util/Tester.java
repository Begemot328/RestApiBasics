package com.epam.esm.persistence.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Tester {
    @Value("${database.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
