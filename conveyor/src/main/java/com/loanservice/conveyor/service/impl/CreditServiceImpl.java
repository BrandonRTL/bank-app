package com.loanservice.conveyor.service.impl;

import com.loanservice.conveyor.exception.CreditDeniedException;
import com.loanservice.conveyor.openapi.dto.*;
import com.loanservice.conveyor.service.CreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A class responsible for scoring data and calculating loan parameters.
 * {@link  CreditService}
 */
@Service
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final RateCalculator rateCalculator;

    public CreditServiceImpl(RateCalculator rateCalculator) {
        this.rateCalculator = rateCalculator;
    }

    /**
     * Performs data scoring. Depending on the input parameters in the scoring data, the loan parameters may change
     * relative to the base rates.
     *
     * @param scoringDataDTO
     * @return CreditDTO with all fields filled in
     */
    @Override
    public CreditDTO score(ScoringDataDTO scoringDataDTO) {
        log.debug("Star score");
        log.debug("Input value: " + scoringDataDTO);
        Map<String, String> error = checkForErrors(scoringDataDTO);
        if (!error.isEmpty()) {
            log.debug("Credit denied");
            throw new CreditDeniedException(error);
        }
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.term(scoringDataDTO.getTerm())
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient());
        BigDecimal rate = rateCalculator.calculateRate(scoringDataDTO.getIsInsuranceEnabled(),
                scoringDataDTO.getIsSalaryClient());
        BigDecimal amount = rateCalculator.calculateAmount(scoringDataDTO.getAmount(),
                scoringDataDTO.getIsInsuranceEnabled(), scoringDataDTO.getIsSalaryClient());

        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.BUSINESS_OWNER) {
            rate = rate.add(BigDecimal.valueOf(0.03));
        }

        if (scoringDataDTO.getEmployment().getPosition() == Position.MIDDLE_MANAGER) {
            rate = rate.subtract(BigDecimal.valueOf(0.02));
        }

        if (scoringDataDTO.getMaritalStatus() == MaritalStatus.DIVORCED) {
            rate = rate.add(BigDecimal.valueOf(0.01));
        }

        if (scoringDataDTO.getDependentAmount() > 1) {
            rate = rate.add(BigDecimal.valueOf(0.01));
        }

        if (scoringDataDTO.getGender().equals(Gender.FEMALE)) {
            rate = rate.subtract(BigDecimal.valueOf(0.01));
        }

        creditDTO.rate(rate).amount(amount);
        BigDecimal monthlyPayment = rateCalculator.calculateMonthlyPayment(creditDTO.getAmount(), creditDTO.getTerm(), creditDTO.getRate());
        creditDTO.setMonthlyPayment(monthlyPayment);
        creditDTO.setPsk(rateCalculator.calculatePsk(creditDTO.getAmount(), creditDTO.getTerm(), creditDTO.getRate()));
        creditDTO.setPaymentSchedule(rateCalculator.calculatePaymentSchedule(creditDTO.getAmount(), creditDTO.getTerm(), creditDTO.getRate()));
        log.debug("End score");
        log.debug("Returned value" + creditDTO);
        return creditDTO;
    }

    public Map<String, String> checkForErrors(ScoringDataDTO scoringDataDTO) {
        log.debug("Start checkForErrors");
        log.debug("Input value: {}", scoringDataDTO);
        Map<String, String> errors = new HashMap<>();
        if (scoringDataDTO.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            errors.put("EmploymentStatus", "UNEMPLOYED");
        }
        log.debug("End checkForErrors");
        log.debug("Returned value: {}", errors);
        return errors;
    }
}
