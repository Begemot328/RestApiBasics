package com.epam.esm.service;

import com.epam.esm.persistence.PersistenceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring config for persistence module.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Configuration
@ComponentScan
@Import(PersistenceConfig.class)
public class ServiceConfig {
}
