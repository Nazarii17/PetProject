package com.tkachuk.pet.exception;

import java.util.NoSuchElementException;

public class NoSuchEntityException extends NoSuchElementException {

    private Throwable cause;

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }
}
