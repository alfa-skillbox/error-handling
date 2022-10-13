package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.repositories;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigurationProperties("errors.repositories.properties")
public class PropertiesBasedErrorsRepository implements ErrorsRepository {

    private Map<String, String> errors = new HashMap<>();
    private Map<String, String> messages = new HashMap<>();

    @PostConstruct
    public void configure() {
        errors.forEach((k, v) -> messages.put(v, k));
    }

    @Override
    public String getErrorMessage(String code) {
        return errors.get(code);
    }

    @Override
    public String getCode(String errorMessage) {
        return messages.get(errorMessage);
    }

    @Override
    public String repositoryType() {
        return "properties";
    }

}
