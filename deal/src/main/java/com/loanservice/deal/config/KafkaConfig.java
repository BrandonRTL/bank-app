package com.loanservice.deal.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic finishRegistrationTopic() {
        return TopicBuilder.name("finish-registration")
                .build();
    }

    @Bean
    public NewTopic createDocumentsTopic() {
        return TopicBuilder.name("create-documents")
                .build();
    }

    @Bean
    public NewTopic sendDocumentsTopic() {
        return TopicBuilder.name("send-documents")
                .build();
    }

    @Bean
    public NewTopic sendSesTopic() {
        return TopicBuilder.name("send-ses")
                .build();
    }

    @Bean
    public NewTopic creditIssuedTopic() {
        return TopicBuilder.name("credit-issued")
                .build();
    }

    @Bean
    public NewTopic applicationDeniedTopic() {
        return TopicBuilder.name("application-denied")
                .build();
    }
}
