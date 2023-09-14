package com.loanservice.deal;

import com.loanservice.deal.model.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = Application.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1,
		topics = {"kafka"})
class DealApplicationTests {

	@Test
	void contextLoads() {
	}

}
