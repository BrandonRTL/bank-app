package com.loanservice.deal.mapper;


import com.loanservice.deal.model.Client;
import com.loanservice.deal.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanApplicationRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "passport.series", source = "passportSeries")
    @Mapping(target = "passport.number", source = "passportNumber")
    Client LoanRequestToClient(LoanApplicationRequestDTO dto);


    @Mapping(target = "passport.issueBranch", source = "passportIssueBranch")
    @Mapping(target = "passport.issueDate", source = "passportIssueDate")
    Client updateRegistrationRequest(@MappingTarget Client client, FinishRegistrationRequestDTO dto);

}
