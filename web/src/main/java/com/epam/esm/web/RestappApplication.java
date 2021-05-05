package com.epam.esm.web;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring boot application class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.epam.esm")
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
