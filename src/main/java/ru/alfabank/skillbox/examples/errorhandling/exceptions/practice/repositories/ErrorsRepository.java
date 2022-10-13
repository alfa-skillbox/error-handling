package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.repositories;


public interface ErrorsRepository {

    String getCode(String errorMessage);

    String getErrorMessage(String code);

    String repositoryType();
}
