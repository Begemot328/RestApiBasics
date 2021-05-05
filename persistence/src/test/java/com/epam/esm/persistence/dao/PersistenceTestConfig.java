package com.epam.esm.persistence.dao;

import com.epam.esm.model.ModelConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@Import(ModelConfig.class)
@ComponentScan("com.epam.esm.persistence")
public class PersistenceTestConfig {
}
