package com.loanservice.deal.mapper;

import com.loanservice.deal.model.Credit;
import com.loanservice.deal.openapi.dto.CreditDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    Credit fromCreditDTO(CreditDTO creditDTO);
}
