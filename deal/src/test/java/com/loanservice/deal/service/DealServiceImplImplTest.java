package com.loanservice.deal.service;

import com.loanservice.deal.exception.DataNotFoundException;
import com.loanservice.deal.feign.ConveyorFeignClient;
import com.loanservice.deal.mapper.ClientMapper;
import com.loanservice.deal.mapper.CreditMapper;
import com.loanservice.deal.mapper.ScoringDataMapper;
import com.loanservice.deal.model.Application;
import com.loanservice.deal.model.Credit;
import com.loanservice.deal.openapi.dto.CreditDTO;
import com.loanservice.deal.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanOfferDTO;
import com.loanservice.deal.repository.ApplicationRepository;
import com.loanservice.deal.repository.ClientRepository;
import com.loanservice.deal.repository.CreditRepository;
import com.loanservice.deal.service.impl.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplImplTest {

    @Mock
    private ConveyorFeignClient feignClient;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditMapper creditMapper;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ScoringDataMapper scoringDataMapper;

    @InjectMocks
    private DealService dealServiceImpl;

    @Test
    void testPrescore() {
        var loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(new LoanOfferDTO().term(10));
        ResponseEntity<List<LoanOfferDTO>> responseFromFeign = ResponseEntity.ok(offers);

        Mockito.when(feignClient.prescore(Mockito.any())).thenReturn(responseFromFeign);
        var res = dealServiceImpl.prescore(loanApplicationRequestDTO);

        Mockito.verify(feignClient, Mockito.times(1)).
                prescore(Mockito.any());
        Mockito.verify(clientRepository, Mockito.times(1)).
                save(Mockito.any());
        Mockito.verify(applicationRepository, Mockito.times(1)).
                save(Mockito.any());
        assertEquals(offers, res);
    }

    @Test
    void testSaveOffer() {
        Application application = new Application();
        application.setStatusHistory(new ArrayList<>());
        LoanOfferDTO offerDTO = new LoanOfferDTO().term(10);
        application.setAppliedOffer(offerDTO);
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(Optional.of(application));
        Mockito.when(applicationRepository.save(Mockito.any())).then(AdditionalAnswers.returnsArgAt(0));

        var resultApplication = dealServiceImpl.saveOffer(offerDTO);

        assertEquals(1, resultApplication.getStatusHistory().size());
        assertEquals(offerDTO, resultApplication.getAppliedOffer());
        Mockito.verify(applicationRepository, Mockito.times(1)).
                findById(Mockito.any());
        Mockito.verify(applicationRepository, Mockito.times(1)).
                save(Mockito.any());
    }

    @Test
    void testFinishRegistration() {
        Application application = new Application();
        application.setAppliedOffer(new LoanOfferDTO());
        var finishReg = new FinishRegistrationRequestDTO();
        var creditDTO = new CreditDTO();
        var feignResponse = ResponseEntity.ok(creditDTO);
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(Optional.of(application));
        Mockito.when(feignClient.score(Mockito.any())).thenReturn(feignResponse);
        Credit credit = new Credit();
        credit.setAmount(BigDecimal.TEN);
        Mockito.when(creditMapper.fromCreditDTO(Mockito.any())).thenReturn(credit);
        Mockito.when(creditRepository.save(Mockito.any())).then(AdditionalAnswers.returnsArgAt(0));
        Mockito.when(applicationRepository.save(Mockito.any())).then(AdditionalAnswers.returnsArgAt(0));

        var resultApplication = dealServiceImpl.finishRegistration(1L, finishReg);

        assertEquals(credit, resultApplication.getCredit());
        Mockito.verify(applicationRepository, Mockito.times(1)).
                findById(Mockito.any());
        Mockito.verify(feignClient, Mockito.times(1)).
                score(Mockito.any());
    }

    @Test
    void testFinishRegistrationApplicationNull() {
        var finishReg = new FinishRegistrationRequestDTO();
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () ->
                dealServiceImpl.finishRegistration(1L, finishReg)
        );
    }

    @Test
    void testFinishRegistrationOfferNull() {
        Application application = new Application();
        var finishReg = new FinishRegistrationRequestDTO();
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(Optional.of(application));

        assertThrows(DataNotFoundException.class, () ->
                dealServiceImpl.finishRegistration(1L, finishReg)
        );
    }

    @Test
    void testSaveOfferApplicationNull() {
        LoanOfferDTO offerDTO = new LoanOfferDTO().term(10);
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () ->
                dealServiceImpl.saveOffer(offerDTO)
        );

    }
}