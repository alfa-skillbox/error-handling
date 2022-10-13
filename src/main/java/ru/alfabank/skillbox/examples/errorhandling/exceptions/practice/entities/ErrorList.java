package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@Accessors(chain = true)
public class ErrorList {

    private final List<Error> errors;

    public ErrorList() {
        errors = new ArrayList<>();
    }

    public ErrorList(Error... errors) {
        this.errors = Arrays.asList(errors);
    }

    public ErrorList(List<Error> errors) {
        this.errors = errors;
    }

    public void add(Error error) {
        errors.add(error);
    }
}
