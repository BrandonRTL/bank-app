package com.loanservice.conveyor.service;


import com.loanservice.conveyor.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.conveyor.openapi.dto.LoanOfferDTO;

import java.util.List;

public interface LoanOfferService {

    List<LoanOfferDTO> createOffers(LoanApplicationRequestDTO loanRequest);
}
