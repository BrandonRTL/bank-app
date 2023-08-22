package com.loanservice.application.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = AdultValidator.class)
@Documented
public @interface Adult {

    String message() default "should be greater than 18";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}