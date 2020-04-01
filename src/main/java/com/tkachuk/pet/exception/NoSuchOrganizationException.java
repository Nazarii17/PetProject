package com.tkachuk.pet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such organization!")
public class NoSuchOrganizationException extends NoSuchElementException {

    public HttpStatus s = HttpStatus.NOT_FOUND;
}
