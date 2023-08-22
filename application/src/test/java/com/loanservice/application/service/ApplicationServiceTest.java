package com.loanservice.application.service;

import com.loanservice.application.feign.DealFeignClient;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private DealFeignClient dealFeignClient;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    void getOffers() {
        LoanApplicationRequestDTO request = new LoanApplicationRequestDTO();
        request.setTerm(5);
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(new LoanOfferDTO());
        ResponseEntity response = ResponseEntity.ok(offers);

        Mockito.when(dealFeignClient.prescore(any())).thenReturn(response);
        assertEquals(offers, applicationService.prescore(request));
    }

    @Test
    void saveOffer() {
        applicationService.saveOffer(new LoanOfferDTO());
        Mockito.verify(dealFeignClient, Mockito.times(1)).chooseOffer(any());
    }
}