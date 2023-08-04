package com.loanservice.conveyor.delegate;


import com.loanservice.conveyor.openapi.api.ConveyorApi;
import com.loanservice.conveyor.openapi.api.ConveyorApiController;
import com.loanservice.conveyor.openapi.api.ConveyorApiDelegate;
import com.loanservice.conveyor.openapi.dto.CreditDTO;
import com.loanservice.conveyor.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.conveyor.openapi.dto.LoanOfferDTO;
import com.loanservice.conveyor.openapi.dto.ScoringDataDTO;
import com.loanservice.conveyor.service.CreditService;
import com.loanservice.conveyor.service.LoanOfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * An implementation of a delegate to be called by the {@link ConveyorApiController}}.
 */
@Service
@Slf4j
public class ConveyorDelegateImpl implements ConveyorApiDelegate {


    private final LoanOfferService offerService;

    private final CreditService creditService;

    public ConveyorDelegateImpl(LoanOfferService offerService, CreditService creditService) {
        this.offerService = offerService;
        this.creditService = creditService;
    }

    /**
     * POST /conveyor/offers : Create 4 loan offers
     *
     * @param loanApplicationRequestDTO
     * @return OK (status code 200)
     *         or Invalid input data (status code 400)
     * @see ConveyorApi#prescore
     */
    @Override
    public ResponseEntity<List<LoanOfferDTO>> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Star prescore");
        log.info("Input value: {}", loanApplicationRequestDTO);
        List<LoanOfferDTO> offers = offerService.createOffers(loanApplicationRequestDTO);
        log.info("End prescore");
        log.info("Returned value: {}", offers);
        return ResponseEntity.ok(offers);
    }

    /**
     * POST /conveyor/calculation : Score offer and calculate loan parameters
     *
     * @param scoringDataDTO
     * @return OK (status code 200)
     *         or Invalid input data (status code 400)
     * @see ConveyorApi#score
     */
    @Override
    public ResponseEntity<CreditDTO> score(ScoringDataDTO scoringDataDTO) {
        log.info("Start score");
        log.info("Input value: {}", scoringDataDTO);
        CreditDTO creditDTO = creditService.score(scoringDataDTO);
        log.info("End score");
        log.info("Returned value: {}", creditDTO);
        return ResponseEntity.ok(creditDTO);
    }
}
