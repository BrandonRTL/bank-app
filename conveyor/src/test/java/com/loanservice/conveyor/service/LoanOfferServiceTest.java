package com.loanservice.conveyor.service;

import com.loanservice.conveyor.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.conveyor.openapi.dto.LoanOfferDTO;
import com.loanservice.conveyor.service.impl.LoanOfferServiceImpl;
import com.loanservice.conveyor.service.impl.RateCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceTest {

    @InjectMocks
    private LoanOfferServiceImpl offerService;

    @Mock
    private RateCalculator calculator;

    @Test
    void createOffers() {
        LoanApplicationRequestDTO request = new LoanApplicationRequestDTO();
        request.setTerm(5);

        BigDecimal rate1 = BigDecimal.valueOf(0.12);
        BigDecimal rate2 = BigDecimal.valueOf(0.13);
        BigDecimal rate3 = BigDecimal.valueOf(0.14);
        BigDecimal rate4 = BigDecimal.valueOf(0.15);
        Mockito.when(calculator.calculateRate(Mockito.anyBoolean(), Mockito.anyBoolean()))
                .thenReturn(rate1, rate2, rate3, rate4);

        List<LoanOfferDTO> offers = offerService.createOffers(request);

        assertEquals(4, offers.size());
        assertEquals(5, offers.get(0).getTerm());
        Mockito.verify(calculator, Mockito.times(4)).calculateRate(Mockito.anyBoolean(), Mockito.anyBoolean());
        assertEquals(rate1, offers.get(0).getRate());
        assertEquals(rate2, offers.get(1).getRate());
        assertEquals(rate3, offers.get(2).getRate());
        assertEquals(rate4, offers.get(3).getRate());
    }
}