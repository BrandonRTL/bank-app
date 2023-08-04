package com.loanservice.conveyor.service;

import com.loanservice.conveyor.openapi.dto.PaymentScheduleElement;
import com.loanservice.conveyor.service.impl.RateCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RateCalculatorTest {

    private BigDecimal testBaseRate = BigDecimal.valueOf(0.12);

    private final RateCalculator rateCalculator = new RateCalculator(testBaseRate);


    @Test
    void calculateRateTrueTrue() {
        BigDecimal res = testBaseRate.add(BigDecimal.valueOf(0.01));
        assertTrue(rateCalculator.calculateRate(true, true).compareTo(res) == 0);
    }

    @Test
    void calculateRateFalseFalse() {
        BigDecimal res = testBaseRate;
        assertTrue(rateCalculator.calculateRate(false, false).compareTo(res) == 0);
    }

    @Test
    void calculateRateTrueFalse() {
        BigDecimal res = testBaseRate.add(BigDecimal.valueOf(0.02));
        assertTrue(rateCalculator.calculateRate(true, false).compareTo(res) == 0);
    }

    @Test
    void calculateRateFalseTrue() {
        BigDecimal res = testBaseRate.subtract(BigDecimal.valueOf(0.01));
        assertTrue(rateCalculator.calculateRate(false, true).compareTo(res) == 0);
    }

    @Test
    void calculateAmountTrueTrue() {
        BigDecimal res = rateCalculator.calculateAmount(BigDecimal.valueOf(10000), true, true);
        assertTrue(res.compareTo(BigDecimal.valueOf(10000)) == 0);
    }

    @Test
    void calculateAmountTrueFalse() {
        BigDecimal res = rateCalculator.calculateAmount(BigDecimal.valueOf(100000), true, false);
        assertTrue(res.compareTo(BigDecimal.valueOf(110000)) == 0);
    }

    @Test
    void calculateMonthlyPayment() {
        BigDecimal res = BigDecimal.valueOf(8.561);
        BigDecimal calculated = rateCalculator.calculateMonthlyPayment(BigDecimal.valueOf(100), 12, BigDecimal.valueOf(0.05));
        assertTrue(calculated.setScale(3, RoundingMode.HALF_UP).compareTo(res.setScale(3, RoundingMode.HALF_UP)) == 0);
    }

    @Test
    void calculatePsk() {
        BigDecimal res = BigDecimal.valueOf(0.2356);
        BigDecimal calculated = rateCalculator.calculatePsk(BigDecimal.valueOf(100000), 3, BigDecimal.valueOf(0.35));
        assertTrue(calculated.setScale(3, RoundingMode.HALF_UP).compareTo(res.setScale(3, RoundingMode.HALF_UP)) == 0);
    }

    @Test
    void calculateSchedule() {
        List<PaymentScheduleElement> schedule = rateCalculator.calculatePaymentSchedule(BigDecimal.valueOf(200000), 24, BigDecimal.valueOf(0.12));
        assertEquals(24, schedule.size());
        BigDecimal payment = schedule.get(0).getTotalPayment();
        BigDecimal truePayment = BigDecimal.valueOf(9415);
        assertTrue(payment.setScale(0, RoundingMode.HALF_UP).compareTo(truePayment) == 0);
    }
}
