package com.loanservice.dossier.service;

import com.loanservice.dossier.feign.DealFeignClient;
import com.loanservice.dossier.openapi.dto.ApplicationDTO;
import com.loanservice.dossier.openapi.dto.EmailMessage;
import com.loanservice.dossier.openapi.dto.EmailTheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageGenerator {

    private final DealFeignClient dealFeignClient;

    public String generateMailSubject(EmailMessage emailMessage) {
        switch (emailMessage.getTheme()) {
            case FINISH_REGISTRATION:
                return "[Loan Service] Finish registration";
            case CREATE_DOCUMENTS:
                return "[Loan Service] Create loan documents";
            case SEND_DOCUMENTS:
                return "[Loan Service] Your loan documents";
            case SEND_SES:
                return "[Loan Service] Ses Code";
            default:
                return "[Loan Service] Error";
        }
    }

    public String generateMailContent(EmailMessage emailMessage) {
        ApplicationDTO applicationDTO;
        switch (emailMessage.getTheme()) {
            case FINISH_REGISTRATION:
                return "Now you need to finish registration";
            case CREATE_DOCUMENTS:
                return "Now you need to prepare your loan documents";
            case SEND_DOCUMENTS:
                applicationDTO = dealFeignClient.getApplicationDTO(emailMessage.getApplicationId()).getBody();
                return "Your loan documents:\n" + applicationDTO.toString();
            case SEND_SES:
                applicationDTO = dealFeignClient.getApplicationDTO(emailMessage.getApplicationId()).getBody();
                return "Your ses code: " + applicationDTO.getSesCode();
            default:
                return "Error";
        }
    }
}
