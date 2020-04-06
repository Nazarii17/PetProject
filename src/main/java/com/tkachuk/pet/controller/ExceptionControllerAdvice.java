package com.tkachuk.pet.controller;

import com.tkachuk.pet.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        //Payload containing exception details
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        //Response entity
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(ApiRequestException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleRuntime(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {NoSuchEntityException.class})
    public ResponseEntity<Object> handleNoSuchEntityException(NoSuchEntityException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {NotSavedException.class})
    public ResponseEntity<Object> handleNotSavedException(NotSavedException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; //TODO???
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {FileException.class})
    public ResponseEntity<Object> handleFileException(FileException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; //TODO???
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

}
