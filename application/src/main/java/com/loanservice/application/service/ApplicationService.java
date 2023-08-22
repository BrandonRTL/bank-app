package com.loanservice.application.service;

import com.loanservice.application.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.application.openapi.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {

    List<LoanOfferDTO> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void saveOffer(LoanOfferDTO dto);
}
