package com.loanservice.application.delegate;

import com.loanservice.application.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.application.openapi.dto.LoanOfferDTO;
import com.loanservice.application.service.impl.ApplicationServiceImpl;
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
class ApplicationDelegateTest {

    @Mock
    private ApplicationServiceImpl applicationService;

    @InjectMocks
    private ApplicationDelegateImpl applicationDelegate;

    @Test
    void prescore() {
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(new LoanOfferDTO());
        Mockito.when(applicationService.prescore(Mockito.any())).thenReturn(offers);
        var response = applicationDelegate.prescore(new LoanApplicationRequestDTO());
        var res = ResponseEntity.ok(offers);
        assertEquals(response, res);
        Mockito.verify(applicationService, Mockito.times(1)).prescore(Mockito.any());
    }

    @Test
    void chooseOffer() {
        var dto = new LoanOfferDTO();
        applicationDelegate.setOffer(dto);
        Mockito.verify(applicationService, Mockito.times(1)).saveOffer(Mockito.any());
    }
}