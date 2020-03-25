package com.tkachuk.pet.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {

    /**
     * Gets errors from bindingResult with addition 'Error' to the end of yhe field;
     * For example name -> nameError;
     *
     * @param bindingResult - Errors according to the properties of the column of the entity;
     * @return - map of "error name" : "error value"
     */
    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
