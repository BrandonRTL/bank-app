package com.loanservice.deal.service.impl;

import com.loanservice.deal.exception.DataNotFoundException;
import com.loanservice.deal.kafka.KafkaProducer;
import com.loanservice.deal.mapper.ApplicationMapper;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    private final ApplicationMapper applicationMapper;

    private final ScoringDataMapper scoringDataMapper;

    private final KafkaProducer kafkaProducer;

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

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setApplicationId(dto.getApplicationId());
        emailMessage.setAddress(application.get().getClient().getEmail());
        emailMessage.setTheme(EmailTheme.FINISH_REGISTRATION);
        kafkaProducer.sendMessage(emailMessage);
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

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setApplicationId(applicationId);
        emailMessage.setAddress(application.get().getClient().getEmail());
        emailMessage.setTheme(EmailTheme.CREATE_DOCUMENTS);
        kafkaProducer.sendMessage(emailMessage);
        log.debug("Saved application: {}", savedApplication);
        return  savedApplication;
    }

    @Override
    public ApplicationDTO getApplicationDTOById(Long applicationId) {
        log.debug("Getting application by id {}", applicationId);
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new DataNotFoundException("Application does not exist");
        }
        log.debug(application.toString());
        return applicationMapper.fromApplication(application.get());
    }

    @Override
    public List<ApplicationDTO> getApplication() {
        List<Application> applications = StreamSupport.stream(applicationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return applicationMapper.fromList(applications);
    }

    @Override
    public void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus) {
        log.debug("Updating application with id {}", applicationId);
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new DataNotFoundException("Application does not exist");
        }
        ApplicationStatusHistory statusHistoryDTO = ApplicationStatusHistory.builder()
                .status(applicationStatus.getValue())
                .timestamp(LocalDateTime.now())
                .type(ChangeType.AUTOMATIC)
                .build();
        application.get().setStatus(applicationStatus);
        application.get().getStatusHistory().add(statusHistoryDTO);
        if (applicationStatus.equals(ApplicationStatus.DOCUMENT_SIGNED)) {
            application.get().setSignDate(LocalDateTime.now());
        }
    }

    @Override
    public void generateSesCode(Long applicationId) {
        log.info("Generating ses code for {}", applicationId);
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new DataNotFoundException("Application does not exist");
        }
        String sesCode = RandomStringUtils.randomAlphabetic(10);
        log.debug("Generated ses code: {}", sesCode);
        application.get().setSesCode(sesCode);
    }

    @Override
    public Boolean verifySesCode(Long applicationId, String sesCode) {
        log.debug("Verifying ses code for {}", applicationId);
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new DataNotFoundException("Application does not exist");
        }
        log.info("Input ses code: {}\n Generated ses code: {}", sesCode, application.get().getSesCode());
        return application.get().getSesCode().equals(sesCode);
    }

    private ScoringDataDTO getScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                          LoanOfferDTO loanOfferDTO, Client client) {
        ScoringDataDTO scoringDataDTO = scoringDataMapper.scoringData(finishRegistrationRequestDTO);
        scoringDataDTO = scoringDataMapper.updateScoringDataOffer(scoringDataDTO, loanOfferDTO);
        scoringDataDTO = scoringDataMapper.updateScoringDataClient(scoringDataDTO, client);
        return scoringDataDTO;
    }
}
