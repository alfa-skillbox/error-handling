package ru.alfabank.skillbox.examples.errorhandling.exceptions.theory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import ru.alfabank.skillbox.examples.errorhandling.repositories.CommonDatabaseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExampleOfCustomHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    public ExampleOfCustomHandlerExceptionResolver() {
        setOrder(0);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof CommonDatabaseException) {
                return handleIllegalArgument((CommonDatabaseException) ex, response);
            }
        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;
    }

    private ModelAndView handleIllegalArgument(CommonDatabaseException ex, HttpServletResponse response) throws IOException {
        String reason = "Service can't connect to database";
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, reason);
        return new ModelAndView();
    }
}
