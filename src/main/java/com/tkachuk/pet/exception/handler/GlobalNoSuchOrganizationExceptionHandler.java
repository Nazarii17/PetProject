package com.tkachuk.pet.exception.handler;

import com.tkachuk.pet.exception.NoSuchOrganizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalNoSuchOrganizationExceptionHandler {

    @ExceptionHandler(NoSuchOrganizationException.class)
    public String NoSuchOrganizationExceptionHandler() {

        return "/exception/noSuchOrganizationException";
    }

}
