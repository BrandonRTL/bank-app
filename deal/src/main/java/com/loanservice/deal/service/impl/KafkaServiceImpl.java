package com.loanservice.deal.service.impl;

import com.loanservice.deal.exception.DataNotFoundException;
import com.loanservice.deal.kafka.KafkaProducer;
import com.loanservice.deal.model.Application;
import com.loanservice.deal.openapi.dto.EmailMessage;
import com.loanservice.deal.openapi.dto.EmailTheme;
import com.loanservice.deal.repository.ApplicationRepository;
import com.loanservice.deal.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    private final KafkaProducer kafkaProducer;

    private final ApplicationRepository applicationRepository;

    @Override
    public void sendBasicMessage(Long applicationId, EmailTheme theme) {
        log.debug("Sending message. {}, {}", applicationId, theme);
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new DataNotFoundException("Application does not exist");
        }

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setAddress(application.get().getClient().getEmail());
        emailMessage.setApplicationId(applicationId);
        emailMessage.setTheme(theme);

        log.debug("Sent message {}", emailMessage);
        kafkaProducer.sendMessage(emailMessage);
    }
}
