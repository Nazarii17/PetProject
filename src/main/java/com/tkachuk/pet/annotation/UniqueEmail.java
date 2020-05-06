package com.tkachuk.pet.annotation;


import com.tkachuk.pet.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface UniqueEmail {

    public abstract String message() default "Email is not unique ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};

}
