package ru.alfabank.skillbox.examples.errorhandling.exceptions.theory;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExampleOfApplicationLayerControllerAdvice {

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ModelAndView handleToLoginPage(HttpServletRequest request) {
        var mav = new ModelAndView(new RedirectView("/login"));
        mav.setStatus(HttpStatus.FOUND);
        return mav;
    }
}
