package com.loanservice.application.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdultValidatorTest {

    private AdultValidator adultValidator = new AdultValidator();

    @Test
    void isValid() {
        LocalDate date = LocalDate.now();
        assertFalse(adultValidator.isValid(date, null));
    }

    @Test
    void isNotValid() {
        LocalDate date = LocalDate.now();
        date = date.minusYears(20);
        assertTrue(adultValidator.isValid(date, null));
    }
}