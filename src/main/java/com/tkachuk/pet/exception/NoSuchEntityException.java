package com.tkachuk.pet.exception;

import java.util.NoSuchElementException;

public class NoSuchEntityException extends NoSuchElementException {

    public NoSuchEntityException(String message) {
        super(message);
    }
}
