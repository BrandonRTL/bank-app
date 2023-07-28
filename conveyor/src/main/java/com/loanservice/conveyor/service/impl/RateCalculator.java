package com.loanservice.conveyor.service.impl;

import com.loanservice.conveyor.openapi.dto.PaymentScheduleElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for performing calculations for creating loan offers and scoring applications
 * Used by {@link CreditServiceImpl} and {@link LoanOfferServiceImpl}
 */
@Service
@Slf4j
public class RateCalculator {

    private final BigDecimal rate;

    public RateCalculator(@Value("${baseRate}") BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * Calculates the modified interest rate depending on the availability of insurance and salary clientship
     * @param isInsuranceEnabled
     * @param isSalaryClient
     * @return modified rate
     */
    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        if (!isInsuranceEnabled && isSalaryClient) {
            return rate.subtract(BigDecimal.valueOf(0.01));
        }
        if (isInsuranceEnabled && !isSalaryClient) {
            return rate.add(BigDecimal.valueOf(0.02));
        }
        if (isInsuranceEnabled && isSalaryClient) {
            return rate.add(BigDecimal.valueOf(0.01));
        }
        return rate;
    }


    /**
     * Calculates the modified amount depending on the availability of insurance and salary clientship
     * @param baseAmount
     * @param isInsuranceEnabled
     * @param isSalaryClient
     * @return modified credit amount
     */
    public BigDecimal calculateAmount(BigDecimal baseAmount, boolean isInsuranceEnabled, boolean isSalaryClient) {
        if (isInsuranceEnabled && !isSalaryClient) {
            return baseAmount.add(BigDecimal.valueOf(10000));
        }
        return baseAmount;
    }
    /**
     * Calculates the amount of the monthly payment depending on the amount of the loan, the term of the loan and the interest rate
     * @param amount
     * @param term loan term in months
     * @param rate
     * @return the amount of the monthly payment
     */
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        log.debug("Calculating monthly payment for amount = {}, term = {}, rate = {}"
        , amount, term, rate);
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);
        BigDecimal numerator = monthlyRate.add(BigDecimal.valueOf(1)).pow(term).multiply(monthlyRate);
        BigDecimal denominator = monthlyRate.add(BigDecimal.valueOf(1)).pow(term).subtract(BigDecimal.ONE);
        log.debug(numerator.toString());
        log.debug(denominator.toString());
        BigDecimal monthlyPayment = amount.multiply(numerator).divide(denominator, 8, RoundingMode.HALF_UP);
        log.debug("Calculated monthly payment {}", monthlyPayment);
        return monthlyPayment;
    }

    /**
     * Calculates psk(Full loan cost) depending on the amount of the loan, the term of the loan and the interest rate
     * @param amount
     * @param term
     * @param rate
     * @return psk
     */
    public BigDecimal calculatePsk(BigDecimal amount, Integer term, BigDecimal rate) {
        log.debug("Calculating psk for amount = {}, term = {}, rate = {}"
                , amount, term, rate);
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, term, rate);
        BigDecimal allAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));
        BigDecimal n = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), 5, RoundingMode.HALF_UP);
        BigDecimal psk = allAmount.divide(amount,5, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)
                .divide(n, 5, RoundingMode.HALF_UP);
        log.debug("Calculated psk {}", psk);
        return psk;
    }

    /**
     * Calculates the loan payment schedule depending on the amount of the loan, the term of the loan and the interest rate
     * @param amount
     * @param term
     * @param rate
     * @return array of elements corresponding to each payout
     */
    public List<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal amount, Integer term, BigDecimal rate) {
        log.debug("Calculating schedule for amount = {}, term = {}, rate = {}"
                , amount, term, rate);
        List<PaymentScheduleElement> schedule = new ArrayList<>();
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 9, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, term, rate);
        LocalDate date = LocalDate.now();
        BigDecimal remaining = amount;
        for(int i = 0; i < term; i++) {
            PaymentScheduleElement element = new PaymentScheduleElement();
            element.setNumber(i);
            element.setDate(date);
            element.setTotalPayment(monthlyPayment);
            element.setInterestPayment(remaining.multiply(monthlyRate));
            element.setDebtPayment(element.getTotalPayment().subtract(element.getInterestPayment()));
            remaining = remaining.subtract(element.getDebtPayment());
            element.setRemainingDebt(remaining);
            date = date.plusMonths(1);
            schedule.add(element);
        }
        log.debug("Calculated schedule {}", schedule);
        return schedule;
    }
}
