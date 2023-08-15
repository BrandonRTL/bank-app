package com.loanservice.deal.mapper;

import com.loanservice.deal.model.Credit;
import com.loanservice.deal.openapi.dto.CreditDTO;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class CreditMapperImplTest {

    private CreditMapper creditMapper = Mappers.getMapper(CreditMapper.class);

    @Test
    void testMapFromCreditDTO() {
        CreditDTO dto = new CreditDTO();
        dto.amount(BigDecimal.ONE)
                .term(6)
                .monthlyPayment(BigDecimal.TEN)
                .rate(BigDecimal.valueOf(0.12))
                .psk(BigDecimal.valueOf(0.13))
                .paymentSchedule(new ArrayList<>());
        Credit credit = creditMapper.fromCreditDTO(dto);
        assertEquals(dto.getAmount(),credit.getAmount());
        assertEquals(dto.getTerm(), credit.getTerm());
        assertEquals(dto.getMonthlyPayment(), credit.getMonthlyPayment());
        assertEquals(dto.getRate(), credit.getRate());
        assertEquals(dto.getPsk(), credit.getPsk());
    }

    @Test
    void testMapNull() {
        CreditDTO dto = null;
        Credit credit = creditMapper.fromCreditDTO(dto);
        assertEquals(null, credit);
    }
}