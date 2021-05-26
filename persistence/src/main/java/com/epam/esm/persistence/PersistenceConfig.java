package com.epam.esm.persistence;

import com.epam.esm.model.ModelConfig;
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
@Import(ModelConfig.class)
public class PersistenceConfig {
}
