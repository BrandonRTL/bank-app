package com.loanservice.deal.mapper;


import com.loanservice.deal.model.Client;
import com.loanservice.deal.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanOfferDTO;
import com.loanservice.deal.openapi.dto.ScoringDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScoringDataMapper {

    ScoringDataDTO scoringData(FinishRegistrationRequestDTO dto);

    @Mapping(target = "amount", source = "totalAmount")
    ScoringDataDTO updateScoringDataOffer(@MappingTarget ScoringDataDTO scoringDataDTO,
                                          LoanOfferDTO offer);

    @Mapping(source = "passport.number", target = "passportNumber")
    @Mapping(source = "passport.series", target = "passportSeries")
    ScoringDataDTO updateScoringDataClient(@MappingTarget ScoringDataDTO scoringDataDTO,
                                          Client client);
}
