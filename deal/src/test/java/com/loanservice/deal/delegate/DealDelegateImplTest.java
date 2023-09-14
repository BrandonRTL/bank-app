package com.loanservice.deal.delegate;

import com.loanservice.deal.openapi.dto.*;
import com.loanservice.deal.service.KafkaService;
import com.loanservice.deal.service.impl.DealService;
import org.checkerframework.checker.units.qual.A;
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

    @Mock
    private KafkaService kafkaService;

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

    @Test
    void testSendDocuments() {
        dealDelegate.sendDocuments(1L);

        Mockito.verify(dealServiceImpl, Mockito.times(1)).
                updateApplicationStatus(1L, ApplicationStatus.PREPARE_DOCUMENTS);

        Mockito.verify(kafkaService, Mockito.times(1)).
                sendBasicMessage(Mockito.any(), Mockito.any());
    }

    @Test
    void testSignDocuments() {
        dealDelegate.signDocuments(1L);

        Mockito.verify(dealServiceImpl, Mockito.times(1)).
                generateSesCode(1L);

        Mockito.verify(kafkaService, Mockito.times(1)).
                sendBasicMessage(Mockito.any(), Mockito.any());
    }

    @Test
    void testVerifyCodeCorrect() {
        Mockito.when(dealServiceImpl.verifySesCode(Mockito.any(), Mockito.any())).thenReturn(true);

        dealDelegate.verifyCode(1L, new SesCodeRequest());

        Mockito.verify(dealServiceImpl, Mockito.times(1)).
                updateApplicationStatus(Mockito.any(), Mockito.any());

        Mockito.verify(kafkaService, Mockito.never()).
                sendBasicMessage(Mockito.any(), Mockito.any());
    }

    @Test
    void testVerifyCodeIncorrect() {
        Mockito.when(dealServiceImpl.verifySesCode(Mockito.any(), Mockito.any())).thenReturn(false);

        dealDelegate.verifyCode(1L, new SesCodeRequest());

        Mockito.verify(dealServiceImpl, Mockito.never()).
                updateApplicationStatus(Mockito.any(), Mockito.any());

        Mockito.verify(kafkaService, Mockito.times(1)).
                sendBasicMessage(Mockito.any(), Mockito.any());
    }

    @Test
    void testGetApplicationById() {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        Mockito.when(dealServiceImpl.getApplicationDTOById(Mockito.any())).thenReturn(applicationDTO);

        var res = dealDelegate.getApplication(1L);
        assertEquals(applicationDTO, res.getBody());
    }
}
