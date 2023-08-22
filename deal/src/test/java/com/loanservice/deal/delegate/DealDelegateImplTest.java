package com.loanservice.deal.delegate;

import com.loanservice.deal.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanOfferDTO;
import com.loanservice.deal.service.impl.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DealDelegateImplTest {

    @Mock
    private DealService dealServiceImpl;

    @InjectMocks
    private DealDelegateImpl dealDelegate;

    @Test
    void testPrescore() {
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(new LoanOfferDTO().term(10));
        Mockito.when(dealServiceImpl.prescore(Mockito.any())).thenReturn(offers);
        var res = dealDelegate.prescore(new LoanApplicationRequestDTO());
        var offersResponse = ResponseEntity.ok(offers);
        assertEquals(offersResponse, res);
        Mockito.verify(dealServiceImpl, Mockito.times(1)).prescore(Mockito.any());
    }

    @Test
    void testSaveOffer() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        dealDelegate.saveOffer(loanOfferDTO);
        Mockito.verify(dealServiceImpl, Mockito.times(1)).
                saveOffer(Mockito.any());
    }

    @Test
    void testFinishRegistration() {
        var requestDTO = new FinishRegistrationRequestDTO();
        dealDelegate.finishRegistration(1L, requestDTO);
        Mockito.verify(dealServiceImpl, Mockito.times(1)).
                finishRegistration(Mockito.any(), Mockito.any());
    }
}