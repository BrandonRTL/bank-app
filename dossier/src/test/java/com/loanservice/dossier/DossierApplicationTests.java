package com.loanservice.dossier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class DossierApplicationTests {

	@MockBean
	private MailSenderValidatorAutoConfiguration autoConfiguration;

	@Test
	void contextLoads() {
	}

}
