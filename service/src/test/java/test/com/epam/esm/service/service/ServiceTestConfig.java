package test.com.epam.esm.service.service;

import com.epam.esm.PersistenceConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@Import(PersistenceConfig.class)
@ComponentScan("com.epam.esm.service")
@ComponentScan("com.epam.esm.persistence")
public class ServiceTestConfig {
}
