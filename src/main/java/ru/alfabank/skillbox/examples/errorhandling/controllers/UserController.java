package ru.alfabank.skillbox.examples.errorhandling.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.alfabank.skillbox.examples.errorhandling.repositories.SqlDataExceptionWrapperException;
import ru.alfabank.skillbox.examples.errorhandling.services.CommonService;

import javax.validation.constraints.Positive;
import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
@Validated
@PreAuthorize("hasRole('USER')")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CommonService service;

    // Case: Bad MediaType or CommonDatabaseException in service layer
    // theory:
    //      returns 415 By DefaultHandlerExceptionResolver if Unsupported Media Type (BasicErrorHandler)
    //      returns 503 By ExampleOfCustomHandlerExceptionResolver if CommonDatabaseException (BasicErrorHandler)
    // practice: returns 500 by ApplicationExceptionHandler
    @PostMapping(value = "/create", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> create(@RequestParam(value = "value") String value) {
        log.info("Create new with value {}", value);
        service.create(value);
        return ResponseEntity.ok().build();
    }

    // Case: SqlDataExceptionWrapperException in service layer
    // theory: returns 500 By ExceptionHandler in UserController class
    // practice: returns 400 by ApplicationExceptionHandler with ConstraintViolationException (id = 0)
    @GetMapping("/read")
    public ResponseEntity<String> read(@AuthenticationPrincipal UserDetails userDetails,
                                       @Positive @RequestParam(value = "id") Integer id) {
        log.info("Read id {} for user {}", id, userDetails.getUsername());
        var val =service.read(userDetails.getUsername(), id);
        return ResponseEntity.ok(id + ":" + val);
    }

    // Case: SqlExceptionWrapperException with @ResponseStatus in service layer
    // theory: returns 500 By @ResponseStatus on Exception (BasicErrorHandler)
    // practice: returns 500 by ApplicationExceptionHandler with SqlExceptionWrapperException
    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable(value = "id") Integer id,
                                         @RequestParam(value = "value") String value) {
        log.info("Update id {}, value {}", id, value);
        String updated = service.update(userDetails.getUsername(), id, value);
        if (updated == null) {
            log.info("Created new id {} with value: {}", id, value);
            return ResponseEntity.created(URI.create("/read/" + id)).build();
        } else {
            log.info("Updated previous value: {} by new value: {} for id: {}", updated, value, id);
            return ResponseEntity.noContent().build();
        }
    }

    @ExceptionHandler(value = {SqlDataExceptionWrapperException.class})
    public ResponseEntity<?> handleCustomExceptions(SqlDataExceptionWrapperException ex, WebRequest request) {
        String msg = String.format("Cause: %s", ex.toString());
        return ResponseEntity.internalServerError().body(msg);
    }
}
