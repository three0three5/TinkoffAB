package org.example.accounts.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.accounts.dto.validator.annotation.ValidBirthday;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ValidBirthdayValidator implements ConstraintValidator<ValidBirthday, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isAfter(LocalDate.now().minus(14, ChronoUnit.YEARS)) &&
                !value.isBefore(LocalDate.now().minus(120, ChronoUnit.YEARS));
    }
}
