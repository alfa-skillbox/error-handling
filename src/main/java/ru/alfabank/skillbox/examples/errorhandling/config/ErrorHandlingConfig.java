package ru.alfabank.skillbox.examples.errorhandling.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.helpers.ErrorBuilder;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.helpers.ExceptionConverter;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.repositories.PropertiesBasedErrorsRepository;

@Configuration
@ConditionalOnProperty(name = "enabled", prefix = "errors.handling", matchIfMissing = true)
@EnableConfigurationProperties(PropertiesBasedErrorsRepository.class)
public class ErrorHandlingConfig {

    @Bean
    public ErrorBuilder errorBuilder() {
        return new ErrorBuilder();
    }

    @Bean
    public ExceptionConverter exceptionConverter(ErrorBuilder errorBuilder) {
        return new ExceptionConverter(errorBuilder);
    }
}
