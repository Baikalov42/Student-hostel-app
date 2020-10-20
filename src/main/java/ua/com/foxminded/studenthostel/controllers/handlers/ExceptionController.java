package ua.com.foxminded.studenthostel.controllers.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {

        LOGGER.error("controller error", ex);
        model.addAttribute("ex", ex);

        return "error";
    }
}
