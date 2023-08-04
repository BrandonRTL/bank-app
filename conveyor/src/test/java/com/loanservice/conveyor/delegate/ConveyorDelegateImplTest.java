package com.loanservice.conveyor.delegate;

import com.loanservice.conveyor.openapi.dto.CreditDTO;
import com.loanservice.conveyor.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.conveyor.openapi.dto.LoanOfferDTO;
import com.loanservice.conveyor.openapi.dto.ScoringDataDTO;
import com.loanservice.conveyor.service.impl.CreditServiceImpl;
import com.loanservice.conveyor.service.impl.LoanOfferServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ConveyorDelegateImplTest {

    @Mock
    private LoanOfferServiceImpl offerService;

    @Mock
    private CreditServiceImpl creditService;

    @InjectMocks
    ConveyorDelegateImpl conveyorDelegate;


    @Test
    void prescore() {
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(new LoanOfferDTO());
        Mockito.when(offerService.createOffers(Mockito.any())).thenReturn(offers);
        var response = conveyorDelegate.prescore(new LoanApplicationRequestDTO());
        var res = ResponseEntity.ok(offers);
        assertEquals(response, res);
        Mockito.verify(offerService, Mockito.times(1)).createOffers(Mockito.any());
    }

    @Test
    void score() {
        CreditDTO creditDTO = new CreditDTO();
        Mockito.when(creditService.score(Mockito.any())).thenReturn(creditDTO);
        conveyorDelegate.score(new ScoringDataDTO());
        Mockito.verify(creditService, Mockito.times(1)).score(Mockito.any());

    }
}