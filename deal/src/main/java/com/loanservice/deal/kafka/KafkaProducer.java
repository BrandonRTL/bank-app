package com.loanservice.deal.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanservice.deal.openapi.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public void sendMessage(EmailMessage message) {
        log.info("Sending to kafka. {}", message);
        try {
            kafkaTemplate.send(message.getTheme().toString(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("Could not parse message to string. Abort sending message");
        }
    }
}
