package com.loanservice.conveyor.service;

import com.loanservice.conveyor.exception.CreditDeniedException;
import com.loanservice.conveyor.openapi.dto.*;
import com.loanservice.conveyor.service.impl.CreditServiceImpl;
import com.loanservice.conveyor.service.impl.RateCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private RateCalculator calculator;

    @InjectMocks
    private CreditServiceImpl creditService;

    @Test
    void score() {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataDTO.setDependentAmount(0);
        scoringDataDTO.setIsInsuranceEnabled(true);
        scoringDataDTO.setIsSalaryClient(true);
        scoringDataDTO.setGender(Gender.MALE);
        EmploymentDTO employmentDTO = new EmploymentDTO();
        scoringDataDTO.setEmployment(employmentDTO);
        BigDecimal baseRate = BigDecimal.valueOf(0.15);
        Mockito.when(calculator.calculateRate(Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(baseRate);

        CreditDTO creditDTO = creditService.score(scoringDataDTO);
        assertEquals(creditDTO.getRate(), baseRate);

        scoringDataDTO.setGender(Gender.FEMALE);
        creditDTO = creditService.score(scoringDataDTO);
        assertEquals(creditDTO.getRate(), baseRate.subtract(BigDecimal.valueOf(0.01)));

        scoringDataDTO.setDependentAmount(2);
        creditDTO = creditService.score(scoringDataDTO);
        assertEquals(creditDTO.getRate(), baseRate.subtract(BigDecimal.valueOf(0.01)).add(BigDecimal.valueOf(0.01)));

        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
        creditDTO = creditService.score(scoringDataDTO);
        assertEquals(creditDTO.getRate(), baseRate.add(BigDecimal.valueOf(0.03)));

        scoringDataDTO.getEmployment().setPosition(Position.MIDDLE_MANAGER);
        creditDTO = creditService.score(scoringDataDTO);
        assertEquals(creditDTO.getRate(), baseRate.add(BigDecimal.valueOf(0.01)));

        scoringDataDTO.setMaritalStatus(MaritalStatus.DIVORCED);
        creditDTO = creditService.score(scoringDataDTO);
        assertEquals(creditDTO.getRate(), baseRate.add(BigDecimal.valueOf(0.02)));

    }

    @Test
    void findErrorsInScoringData() {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        EmploymentDTO employmentDTO = new EmploymentDTO();
        employmentDTO.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        scoringDataDTO.setEmployment(employmentDTO);

        assertEquals(1, creditService.checkForErrors(scoringDataDTO).size());

        assertThrows(CreditDeniedException.class, () -> creditService.score(scoringDataDTO));
    }


}