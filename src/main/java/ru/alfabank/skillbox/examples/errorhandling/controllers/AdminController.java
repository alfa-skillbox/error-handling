package ru.alfabank.skillbox.examples.errorhandling.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.skillbox.examples.errorhandling.services.CommonService;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final CommonService service;

    // Case: AccessDeniedException by PreAuthorize control
    // theory: returns 302 By ExampleOfApplicationLayerControllerAdvice
    // practice: returns 500 by ApplicationExceptionHandler
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal Authentication authentication,
                                         @RequestParam(value = "id") Integer id) {
        log.info("Delete id {}", id);
        var deleted = service.delete(authentication, id);
        if (deleted != null) {
            log.info("Deleted id: {} with value: {} by user {}", id, deleted, authentication.getName());
            return ResponseEntity.status(200).body(deleted);
        } else {
            log.info("Can't delete id: {} by user {}. Resource not found", id, authentication.getName());
            return ResponseEntity.notFound().build();
        }
    }
}
