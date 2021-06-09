package com.epam.esm;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring config for persistence module.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Configuration
@ComponentScan
@EnableJpaRepositories
@ComponentScan(value = "com.epam.esm")
@EntityScan
@EnableJpaAuditing
public class PersistenceConfig {
}