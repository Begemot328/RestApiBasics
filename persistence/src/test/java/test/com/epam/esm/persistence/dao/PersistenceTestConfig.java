package test.com.epam.esm.persistence.dao;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@EnableAutoConfiguration
public class PersistenceTestConfig {

}
