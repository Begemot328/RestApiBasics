package com.epam.esm.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring config for model module.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Configuration
@ComponentScan(value = "com.epam.esm.model")
@EntityScan
public class ModelConfig {
}
