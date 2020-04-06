package com.tkachuk.pet.exception.handler;

import com.tkachuk.pet.exception.NoSuchEntityException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalNoSuchEntityExceptionHandler {

    @ExceptionHandler(NoSuchEntityException.class)
    public String NoSuchEntityExceptionHandler() {

        return "/exception/noSuchEntityException";
    }

}
