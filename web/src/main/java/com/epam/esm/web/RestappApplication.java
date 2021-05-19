package com.epam.esm.web;

import com.epam.esm.service.ServiceConfig;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Spring boot application class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.epam.esm.web")
@Import(ServiceConfig.class)
public class RestappApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestappApplication.class, args);
    }

    /**
     * {@link ModelMapper} Bean creator.
     *
     * @return {@link ModelMapper} Bean.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
}
