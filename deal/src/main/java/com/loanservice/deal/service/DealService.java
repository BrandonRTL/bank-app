package com.loanservice.deal.service;

import com.loanservice.deal.model.Application;
import com.loanservice.deal.openapi.dto.*;

import java.util.List;

public interface DealService {

    List<LoanOfferDTO> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO);

    Application saveOffer(LoanOfferDTO dto);

    Application finishRegistration(Long applicationId,
                                   FinishRegistrationRequestDTO finishRegistrationRequestDTO);

    ApplicationDTO getApplicationDTOById(Long applicationId);

    void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus);

    void generateSesCode(Long applicationId);

    Boolean verifySesCode(Long applicationId, String sesCode);
}
