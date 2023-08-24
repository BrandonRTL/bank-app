package com.loanservice.application.delegate;

import com.loanservice.application.openapi.api.ApplicationApiDelegate;
import com.loanservice.application.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.application.openapi.dto.LoanOfferDTO;
import com.loanservice.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationDelegateImpl implements ApplicationApiDelegate {

    private final ApplicationService applicationService;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Start prescoring. Input data: {}", loanApplicationRequestDTO);
        var offers = applicationService.prescore(loanApplicationRequestDTO);
        log.info("End prescoring. Offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<Void> setOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Chosen offer: {}", loanOfferDTO);
        applicationService.saveOffer(loanOfferDTO);
        log.info("End saving");
        return ResponseEntity.ok().build();
    }
}
