package com.loanservice.gateway.delegate;

import com.loanservice.gateway.openapi.api.GatewayApiDelegate;
import com.loanservice.gateway.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.gateway.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.gateway.openapi.dto.LoanOfferDTO;
import com.loanservice.gateway.openapi.dto.SesCodeRequest;
import com.loanservice.gateway.service.GatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GatewayDelegateImpl implements GatewayApiDelegate {

    private final GatewayService gatewayService;
    @Override
    public ResponseEntity<List<LoanOfferDTO>> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Creating loan application. {}", loanApplicationRequestDTO);
        var offers = gatewayService.prescore(loanApplicationRequestDTO);
        log.info("Offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<Void> saveOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Applying offer. {}", loanOfferDTO);
        gatewayService.applyOffer(loanOfferDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> finishRegistration(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("Finishing registration. {}", finishRegistrationRequestDTO);
        gatewayService.finishRegistration(applicationId, finishRegistrationRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> requestDocuments(Long applicationId) {
        log.info("Sending documents to {}", applicationId);
        gatewayService.sendDocuments(applicationId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signDocuments(Long applicationId) {
        log.info("Signing documents to {}", applicationId);
        gatewayService.signDocuments(applicationId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> verifyCode(Long applicationId, SesCodeRequest sesCodeRequest) {
        log.info("Verifying code {} for {}", sesCodeRequest.getSesCode(), applicationId);
        gatewayService.verifyCode(applicationId, sesCodeRequest);
        return ResponseEntity.ok().build();
    }
}
