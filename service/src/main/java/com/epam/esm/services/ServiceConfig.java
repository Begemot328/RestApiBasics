package com.epam.esm.services;

import com.epam.esm.persistence.PersistenceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.epam.esm.services")
@Import(PersistenceConfig.class)
public class ServiceConfig {
}


