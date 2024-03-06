package org.example.accounts.dto.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.accounts.dto.validator.ValidBirthdayValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.example.accounts.utils.Constants.INVALID_BIRTHDAY_MESSAGE;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidBirthdayValidator.class)
public @interface ValidBirthday {
    String message() default INVALID_BIRTHDAY_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
