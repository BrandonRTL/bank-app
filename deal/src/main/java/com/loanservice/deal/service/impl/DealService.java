package com.loanservice.deal.service.impl;

import com.loanservice.deal.exception.DataNotFoundException;
import com.loanservice.deal.model.Application;
import com.loanservice.deal.model.ApplicationStatusHistory;
import com.loanservice.deal.model.Client;
import com.loanservice.deal.model.Credit;
import com.loanservice.deal.openapi.dto.*;
import com.loanservice.deal.repository.ApplicationRepository;
import com.loanservice.deal.repository.ClientRepository;
import com.loanservice.deal.feign.ConveyorFeignClient;
import com.loanservice.deal.mapper.ClientMapper;
import com.loanservice.deal.mapper.CreditMapper;
import com.loanservice.deal.mapper.ScoringDataMapper;
import com.loanservice.deal.repository.CreditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class DealService implements com.loanservice.deal.service.DealService {

    private final ConveyorFeignClient feignClient;

    private final ClientRepository clientRepository;

    private final ApplicationRepository applicationRepository;

    private final CreditRepository creditRepository;

    private final CreditMapper creditMapper;

    private final ClientMapper clientMapper;

    private final ScoringDataMapper scoringDataMapper;

    @Override
    public List<LoanOfferDTO> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("Start prescoring. Input: {}", loanApplicationRequestDTO);
        var res = feignClient.prescore(loanApplicationRequestDTO);
        Client client = clientMapper.LoanRequestToClient(loanApplicationRequestDTO);
        clientRepository.save(client);
        Application application = Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .statusHistory(new ArrayList<>())
                .build();
        applicationRepository.save(application);
        for (LoanOfferDTO dto : res.getBody()) {
            dto.setApplicationId(application.getApplicationId());
        }
        log.debug("Prescoring finished. Result: {}", res.getBody());
        return res.getBody();
    }

    @Override
    public Application saveOffer(LoanOfferDTO dto) {
        log.debug("Start saving. Offer: {}", dto);
        Optional<Application> application = applicationRepository.findById(dto.getApplicationId());
        if (application.isEmpty()) {
            throw new DataNotFoundException("Application does not exist");
        }
        application.get().setAppliedOffer(dto);
        ApplicationStatusHistory statusHistoryDTO = ApplicationStatusHistory.builder()
                .status(ApplicationStatus.APPROVED.getValue())
                .timestamp(LocalDateTime.now())
                .type(ChangeType.AUTOMATIC)
                .build();
        application.get().getStatusHistory().add(statusHistoryDTO);
        application.get().setStatus(ApplicationStatus.APPROVED);
        var savedApplication = applicationRepository.save(application.get());
        log.debug("Finished saving");
        return savedApplication;
    }

    @Override
    public Application finishRegistration(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.debug("Finishing registration. {}", finishRegistrationRequestDTO);
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new DataNotFoundException("Application does not exist");
        }
        if (application.get().getAppliedOffer() == null) {
            throw new DataNotFoundException("Application does not have offer");
        }
        System.out.println(application.get().getAppliedOffer());
        Client client = application.get().getClient();
        client = clientMapper.updateRegistrationRequest(client, finishRegistrationRequestDTO);
        clientRepository.save(client);
        ScoringDataDTO scoringDataDTO = getScoringData(finishRegistrationRequestDTO,
                application.get().getAppliedOffer(), application.get().getClient());
        CreditDTO creditDTO = feignClient.score(scoringDataDTO).getBody();

        Credit credit = creditMapper.fromCreditDTO(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        creditRepository.save(credit);
        application.get().setCredit(credit);
        var savedApplication = applicationRepository.save(application.get());
        log.debug("Saved application: {}", savedApplication);
        return  savedApplication;
    }

    private ScoringDataDTO getScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                          LoanOfferDTO loanOfferDTO, Client client) {
        ScoringDataDTO scoringDataDTO = scoringDataMapper.scoringData(finishRegistrationRequestDTO);
        scoringDataDTO = scoringDataMapper.updateScoringDataOffer(scoringDataDTO, loanOfferDTO);
        scoringDataDTO = scoringDataMapper.updateScoringDataClient(scoringDataDTO, client);
        return scoringDataDTO;
    }
}
