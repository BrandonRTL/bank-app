package com.loanservice.deal.mapper;

import com.loanservice.deal.model.Client;
import com.loanservice.deal.openapi.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientMapperTest {

    private ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @Test
    void testMapRequestToClient() {
        var loanApp = new LoanApplicationRequestDTO();
        loanApp.lastName("string").firstName("123").middleName("d").email("asd@mail.ru");
        Client client = clientMapper.LoanRequestToClient(loanApp);
        assertEquals(loanApp.getFirstName(), client.getFirstName());
        assertEquals(loanApp.getLastName(), client.getLastName());
        assertEquals(loanApp.getMiddleName(), client.getMiddleName());
        assertEquals(loanApp.getEmail(), client.getEmail());
    }

    @Test
    void testMapNull() {
        LoanApplicationRequestDTO loanApp = null;
        Client client = clientMapper.LoanRequestToClient(loanApp);
        assertEquals(client, null);
    }

    @Test
    void testUpdate() {
        var dto = new FinishRegistrationRequestDTO();
        dto.gender(Gender.MALE)
                .maritalStatus(MaritalStatus.DIVORCED)
                .dependentAmount(3)
                .employment(new EmploymentDTO())
                .account("123");
        Client client = new Client();
        clientMapper.updateRegistrationRequest(client, dto);
        assertEquals(client.getGender(), dto.getGender());
        assertEquals(client.getMaritalStatus(), dto.getMaritalStatus());
        assertEquals(client.getDependentAmount(), dto.getDependentAmount());
        assertEquals(client.getAccount(), dto.getAccount());
        assertEquals(client.getEmployment(), dto.getEmployment());
    }

    @Test
    void testUpdateNull() {
        Client client = Client.builder().account("123").build();
        FinishRegistrationRequestDTO dto = null;
        clientMapper.updateRegistrationRequest(client, dto);
        assertEquals(null, client.getGender());
    }
}