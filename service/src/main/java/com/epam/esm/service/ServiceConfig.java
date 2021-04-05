package com.epam.esm.service;

import com.epam.esm.persistence.PersistenceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.epam.esm.service")
@Import(PersistenceConfig.class)
@ComponentScan("com.epam.esm.service")
public class ServiceConfig {
}


