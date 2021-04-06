package com.epam.esm.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan("com.epam.esm.persistence")
@PropertySource("classpath:database.properties")
public class PersistenceConfig {

    @Bean
    public static JdbcTemplate getTemplate(BasicDataSource source) {
        return new JdbcTemplate(source);
    }

}


