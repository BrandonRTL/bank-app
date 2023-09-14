package com.loanservice.deal.service;

import com.loanservice.deal.kafka.KafkaProducer;
import com.loanservice.deal.model.Application;
import com.loanservice.deal.model.Client;
import com.loanservice.deal.openapi.dto.EmailTheme;
import com.loanservice.deal.repository.ApplicationRepository;
import com.loanservice.deal.service.impl.DealService;
import com.loanservice.deal.service.impl.KafkaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.meta.When;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class KafkaServiceImplTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private KafkaServiceImpl kafkaService;

    @Test
    void testSendBasicMessage() {
        Application application = new Application();
        application.setStatusHistory(new ArrayList<>());
        Client client = new Client();
        client.setEmail("123@mail.com");
        application.setClient(client);
        Mockito.when(applicationRepository.findById(Mockito.any())).thenReturn(Optional.of(application));

        kafkaService.sendBasicMessage(1L, EmailTheme.FINISH_REGISTRATION);

        Mockito.verify(kafkaProducer, Mockito.times(1)).
                sendMessage(Mockito.any());
        Mockito.verify(applicationRepository, Mockito.times(1)).
                findById(Mockito.any());
    }
}