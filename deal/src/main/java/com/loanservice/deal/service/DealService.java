package com.loanservice.deal.service;

import com.loanservice.deal.model.Application;
import com.loanservice.deal.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanOfferDTO;

import java.util.List;

public interface DealService {

    List<LoanOfferDTO> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO);

    Application saveOffer(LoanOfferDTO dto);

    Application finishRegistration(Long applicationId,
                                   FinishRegistrationRequestDTO finishRegistrationRequestDTO);

}
