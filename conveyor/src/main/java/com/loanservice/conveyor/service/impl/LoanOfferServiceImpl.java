package com.loanservice.conveyor.service.impl;


import com.loanservice.conveyor.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.conveyor.openapi.dto.LoanOfferDTO;
import com.loanservice.conveyor.service.LoanOfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for creation of loan offers
 * {@link LoanOfferService}
 */
@Service
@Slf4j
public class LoanOfferServiceImpl implements LoanOfferService {
    private final RateCalculator rateCalculator;

    public LoanOfferServiceImpl(RateCalculator rateCalculator) {
        this.rateCalculator = rateCalculator;
    }

    /**
     * Creates loan offers for input LoanRequest.
     * For each LoanRequest, four proposals are created with different values for the fields isInsuranceEnabled
     * and isSalaryClient
     * @param loanRequest
     * @return List of loan offers
     */
    @Override
    public List<LoanOfferDTO> createOffers(LoanApplicationRequestDTO loanRequest) {
        log.debug("Creating offer list");
        log.debug("LoanRequest {}", loanRequest);
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(createOffer(loanRequest, false, true));
        offers.add(createOffer(loanRequest, false, false));
        offers.add(createOffer(loanRequest, true, true));
        offers.add(createOffer(loanRequest, true, false));
        log.debug("Returned value {}", offers);
        return offers;
    }

    private LoanOfferDTO createOffer(LoanApplicationRequestDTO loadRequest,
                                     boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.debug("Creating offer");
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setIsInsuranceEnabled(isInsuranceEnabled);
        loanOfferDTO.setIsSalaryClient(isSalaryClient);
        loanOfferDTO.setRequestedAmount(loadRequest.getAmount());
        loanOfferDTO.setTerm(loadRequest.getTerm());
        loanOfferDTO.setTotalAmount(rateCalculator.calculateAmount(loadRequest.getAmount(), isInsuranceEnabled, isSalaryClient));
        loanOfferDTO.setRate(rateCalculator.calculateRate(isInsuranceEnabled, isSalaryClient));
        loanOfferDTO.setMonthlyPayment(rateCalculator.calculateMonthlyPayment(
                loanOfferDTO.getTotalAmount(), loanOfferDTO.getTerm(),loanOfferDTO.getRate()));
        log.debug("Returned value {}", loanOfferDTO);
        return loanOfferDTO;
    }

}
