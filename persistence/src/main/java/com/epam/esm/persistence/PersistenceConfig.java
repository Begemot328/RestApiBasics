package com.epam.esm.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan("com.epam.esm.persistence")
@PropertySource("classpath:database.properties")
public class PersistenceConfig {
    @Value("${database.url}")
    private String url;

    @Bean
    public static JdbcTemplate getTemplate(BasicDataSource source) {
        return new JdbcTemplate(source);
    }

}


