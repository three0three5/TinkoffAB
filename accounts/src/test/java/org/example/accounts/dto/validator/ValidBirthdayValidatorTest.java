package org.example.accounts.dto.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ValidBirthdayValidatorTest {
    public final ValidBirthdayValidator validator = new ValidBirthdayValidator();

    @Mock
    public ConstraintValidatorContext validatorContext;

    @Test
    void givenDateAfterNow_whenValidate_thenFalse() {
        assertFalse(validator.isValid(LocalDate.now().plus(1, ChronoUnit.DAYS), validatorContext));
    }

    @Test
    void givenDateWith3000Year_whenValidate_thenFalse() {
        assertFalse(validator.isValid(LocalDate.now().plus(1000, ChronoUnit.YEARS), validatorContext));
    }
}