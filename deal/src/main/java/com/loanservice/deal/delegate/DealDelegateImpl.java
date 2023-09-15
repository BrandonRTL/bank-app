package com.loanservice.deal.delegate;


import com.loanservice.deal.openapi.api.DealApiDelegate;
import com.loanservice.deal.openapi.dto.*;
import com.loanservice.deal.service.DealService;
import com.loanservice.deal.service.KafkaService;
import com.loanservice.deal.service.impl.KafkaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * An implementation of a delegate to be called by the {@link com.loanservice.deal.openapi.api.DealApiController}}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DealDelegateImpl implements DealApiDelegate {

    private final DealService dealService;

    private final KafkaService kafkaService;

    /**
     * POST /deal/application : Get 4 loan offers from conveyor service
     *
     * @param loanApplicationRequestDTO
     * @return OK (status code 200)
     *         or Invalid input data (status code 400)
     * @see com.loanservice.deal.openapi.api.DealApi#prescore
     */
    @Override
    public ResponseEntity<List<LoanOfferDTO>> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Start prescoring. Input data: {}", loanApplicationRequestDTO);
        var offers = dealService.prescore(loanApplicationRequestDTO);
        log.info("End prescoring. Offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    /**
     * PUT /deal/offer : Select one offer from the proposed ones
     *
     * @param loanOfferDTO
     * @return OK (status code 200)
     */
    @Override
    public ResponseEntity<Void> saveOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Saved offer: {}", loanOfferDTO);
        var result = dealService.saveOffer(loanOfferDTO);
        log.info("End saving. Result application: {}", result);
        return ResponseEntity.ok().build();
    }


    /**
     * PUT /deal/calculate/{applicationId} : Saturate the entities with all the
     * parameters and make a full calculation of the loan
     *
     * @param applicationId
     * @param finishRegistrationRequestDTO
     * @return OK (status code 200)
     *         or Invalid input data (status code 400)
     */
    @Override
    public ResponseEntity<Void> finishRegistration(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("Finish registration. Input data: {}", finishRegistrationRequestDTO);
        dealService.finishRegistration(applicationId, finishRegistrationRequestDTO);
        log.info("Registration finished.");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> sendDocuments(Long applicationId) {
        log.info("Sending send document request");
        dealService.updateApplicationStatus(applicationId, ApplicationStatus.PREPARE_DOCUMENTS);
        kafkaService.sendBasicMessage(applicationId, EmailTheme.SEND_DOCUMENTS);
        log.info("Request sent");
        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<Void> signDocuments(Long applicationId) {
        log.info("Sending sign document request");
        dealService.generateSesCode(applicationId);
        kafkaService.sendBasicMessage(applicationId, EmailTheme.SEND_SES);
        log.info("Request sent");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> verifyCode(Long applicationId, SesCodeRequest sesCodeRequest) {
        log.info("Verifying ses code {}", sesCodeRequest.getSesCode());
        if (!dealService.verifySesCode(applicationId, sesCodeRequest.getSesCode())) {
            kafkaService.sendBasicMessage(applicationId, EmailTheme.CREDIT_ISSUED);
        } else {
            dealService.updateApplicationStatus(applicationId, ApplicationStatus.DOCUMENT_SIGNED);

        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApplicationDTO> getApplication(Long applicationId) {
        log.info("Get application with id {}", applicationId);
        var application = dealService.getApplicationDTOById(applicationId);
        log.info(application.toString());
        return ResponseEntity.ok(application);
    }

    @Override
    public ResponseEntity<List<ApplicationDTO>> getApplications() {
        log.info("Get applications");
        return ResponseEntity.ok(dealService.getApplication());
    }

    @Override
    public ResponseEntity<Void> updateApplication(Long applicationId) {
        log.info("Start updating application with id {}", applicationId);
        dealService.updateApplicationStatus(applicationId, ApplicationStatus.DOCUMENTS_CREATED);
        return ResponseEntity.ok().build();
    }


}
