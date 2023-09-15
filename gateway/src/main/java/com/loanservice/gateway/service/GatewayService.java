package com.loanservice.gateway.service;

import com.loanservice.gateway.feign.ApplicationFeignClient;
import com.loanservice.gateway.feign.DealFeignClient;
import com.loanservice.gateway.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.gateway.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.gateway.openapi.dto.LoanOfferDTO;
import com.loanservice.gateway.openapi.dto.SesCodeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GatewayService {

    private final ApplicationFeignClient applicationFeignClient;

    private final DealFeignClient dealFeignClient;

    public List<LoanOfferDTO> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("Prescoring, {}", loanApplicationRequestDTO);
        var res = applicationFeignClient.prescore(loanApplicationRequestDTO);
        log.debug("Return {}", res);
        return res.getBody();
    }

    public void applyOffer(LoanOfferDTO offerDTO) {
        log.debug("Applying offer. {}", offerDTO);
        applicationFeignClient.applyOffer(offerDTO);
        log.debug("Offer applied");
    }

    public void finishRegistration(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.debug("Finishing registration. {}", finishRegistrationRequestDTO);
        dealFeignClient.finishRegistration(applicationId, finishRegistrationRequestDTO);
        log.debug("Registration finished");
    }

    public void sendDocuments(Long applicationId) {
        log.debug("Sending documents, id = {}",applicationId);
        dealFeignClient.sendDocuments(applicationId);
    }

    public void signDocuments(Long applicationId) {
        log.debug("Signing documents, id = {}",applicationId);
        dealFeignClient.signDocuments(applicationId);
    }

    public void verifyCode(Long applicationId, SesCodeRequest sesCodeRequest) {
        log.debug("Verifying documents, id = {}, code = {}",applicationId, sesCodeRequest.getSesCode());
        dealFeignClient.verifySES(applicationId, sesCodeRequest);
    }
}
