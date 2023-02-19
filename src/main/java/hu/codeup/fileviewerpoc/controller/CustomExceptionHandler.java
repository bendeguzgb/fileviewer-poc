package hu.codeup.fileviewerpoc.controller;

import lombok.val;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        return setErrorMessage("Access Denied", e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingRequestParamException(MissingServletRequestParameterException e) {
        return setErrorMessage("Missing Parameter", e.getMessage());
    }

    private ModelAndView setErrorMessage(String type, String message) {
        val modelView = new ModelAndView();
        modelView.setViewName("custom_error");
        modelView.addObject("type", type);
        modelView.addObject("message", message);
        return modelView;
    }
}
