package com.loanservice.deal.service;

import com.loanservice.deal.openapi.dto.EmailTheme;

public interface KafkaService {

    public void sendBasicMessage(Long applicationId, EmailTheme theme);
}
