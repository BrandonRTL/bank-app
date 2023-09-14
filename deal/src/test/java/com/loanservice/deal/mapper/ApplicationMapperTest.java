package com.loanservice.deal.mapper;

import com.loanservice.deal.model.Application;
import com.loanservice.deal.model.Credit;
import com.loanservice.deal.openapi.dto.ApplicationDTO;
import com.loanservice.deal.openapi.dto.CreditDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ApplicationMapperTest {

    private ApplicationMapper applicationMapper = Mappers.getMapper(ApplicationMapper.class);

    @Test
    void DTOfromApplication() {
        Application application = new Application();
        Credit credit = new Credit();
        credit.setAmount(BigDecimal.ONE);
        application.setCredit(credit);

        ApplicationDTO dto = applicationMapper.fromApplication(application);
        assertEquals(credit.getAmount(), dto.getCredit().getAmount());
    }

}
