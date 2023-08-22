package com.loanservice.deal.mapper;

import com.loanservice.deal.model.Client;
import com.loanservice.deal.model.Passport;
import com.loanservice.deal.openapi.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScoringDataMapperTest {

    private ScoringDataMapper scoringDataMapper = Mappers.getMapper(ScoringDataMapper.class);

    @Test
    void testMapFinishRegistrationDTO() {
        var dto = new FinishRegistrationRequestDTO();
        dto.gender(Gender.MALE)
                .passportIssueBranch("123")
                .passportIssueDate(LocalDate.EPOCH)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(10)
                .account("23423");
        ScoringDataDTO scoringData = scoringDataMapper.scoringData(dto);
        assertEquals(dto.getGender(), scoringData.getGender());
        assertEquals(dto.getPassportIssueDate(), scoringData.getPassportIssueDate());
        assertEquals(dto.getPassportIssueBranch(), scoringData.getPassportIssueBranch());
        assertEquals(dto.getDependentAmount(), scoringData.getDependentAmount());
        assertEquals(dto.getMaritalStatus(), scoringData.getMaritalStatus());
        assertEquals(dto.getAccount(), scoringData.getAccount());
    }

    @Test
    void testMapFinishRegistrationDTONull() {
        ScoringDataDTO scoringDataDTO = scoringDataMapper.scoringData(null);
        assertEquals(null, scoringDataDTO);
    }

    @Test
    void testUpdateFromOffer() {
        LoanOfferDTO offerDTO = new LoanOfferDTO();
        offerDTO.totalAmount(BigDecimal.TEN)
                .term(10)
                .isInsuranceEnabled(true)
                .isSalaryClient(true);
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataMapper.updateScoringDataOffer(scoringDataDTO, offerDTO);
        assertEquals(offerDTO.getTotalAmount(), scoringDataDTO.getAmount());
        assertEquals(offerDTO.getTerm(), scoringDataDTO.getTerm());
        assertEquals(offerDTO.getIsSalaryClient(),scoringDataDTO.getIsSalaryClient());
        assertEquals(offerDTO.getIsInsuranceEnabled(), scoringDataDTO.getIsInsuranceEnabled());
    }

    @Test
    void testUpdateFromOfferNull() {
        LoanOfferDTO offerDTO = null;
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataMapper.updateScoringDataOffer(scoringDataDTO, offerDTO);
        assertEquals(null, scoringDataDTO.getAmount());
        assertEquals(null, scoringDataDTO.getTerm());
        assertEquals(null,scoringDataDTO.getIsSalaryClient());
        assertEquals(null, scoringDataDTO.getIsInsuranceEnabled());
    }

    @Test
    void testUpdateFromClient() {
        Client client = Client.builder()
                .firstName("sdf")
                .lastName("bfg")
                .dependentAmount(12)
                .build();
        Passport passport = new Passport();
        passport.setNumber("123");
        client.setPassport(passport);
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataMapper.updateScoringDataClient(scoringDataDTO, client);
        assertEquals(client.getFirstName(), scoringDataDTO.getFirstName());
        assertEquals(client.getLastName(), scoringDataDTO.getLastName());
        assertEquals(client.getDependentAmount(), scoringDataDTO.getDependentAmount());
        assertEquals(client.getPassport().getNumber(), scoringDataDTO.getPassportNumber());
    }

    @Test
    void testUpdateFromClientNull() {
        Client client = null;
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataMapper.updateScoringDataClient(scoringDataDTO, client);
        assertEquals(null, scoringDataDTO.getFirstName());
        assertEquals(null, scoringDataDTO.getLastName());
        assertEquals(null, scoringDataDTO.getDependentAmount());
    }
}