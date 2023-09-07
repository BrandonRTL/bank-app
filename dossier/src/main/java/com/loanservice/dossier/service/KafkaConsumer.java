package com.loanservice.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanservice.dossier.feign.DealFeignClient;
import com.loanservice.dossier.openapi.dto.ApplicationDTO;
import com.loanservice.dossier.openapi.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MailService mailService;

    private final MessageGenerator messageGenerator;

    private final DealFeignClient dealFeignClient;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topics.finish-registration}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFinishRegistrationMessage(String message) throws JsonProcessingException {
        generateAndSend(message);
    }

    @KafkaListener(topics = "${spring.kafka.topics.create-documents}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCreateDocumentsMessage(String message) throws JsonProcessingException{
        generateAndSend(message);
    }

    @KafkaListener(topics = "${spring.kafka.topics.send-documents}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSendDocumentsMessage(String message) throws JsonProcessingException {
        generateAndSend(message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        dealFeignClient.updateApplicationStatus(emailMessage.getApplicationId());
    }

    @KafkaListener(topics = "${spring.kafka.topics.send-ses}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSendSesMessage(String message) throws JsonProcessingException {
        generateAndSend(message);
    }

    private void generateAndSend(String message) throws JsonProcessingException {
        log.info("Got kafka message {}", message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        String mailSubj = messageGenerator.generateMailSubject(emailMessage);
        String mailMessage = messageGenerator.generateMailContent(emailMessage);
        mailService.send(emailMessage.getAddress(), mailSubj, mailMessage);
        log.info("Message sent");
    }

    @KafkaListener(topics = "${spring.kafka.topics.credit-issued}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCreditIssuedMessage(String message) {
        log.info(message);
    }

    @KafkaListener(topics = "application-denied", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeApplicationDeniedMessage(String message) {
        log.info(message);
    }


}
