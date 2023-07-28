package com.loanservice.conveyor.controller;


import com.loanservice.conveyor.delegate.ConveyorDelegateImpl;
import com.loanservice.conveyor.openapi.api.ConveyorApiController;
import com.loanservice.conveyor.service.CreditService;
import com.loanservice.conveyor.service.LoanOfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConveyorApiController.class)
@Import(ConveyorDelegateImpl.class)
public class ConveyorApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanOfferService loanOfferService;

    @MockBean
    private CreditService creditService;

    @Test
    void prescore() throws Exception {
        String json = "{\n" +
                "  \"amount\": 10000,\n" +
                "  \"term\": 6,\n" +
                "  \"firstName\": \"string\",\n" +
                "  \"lastName\": \"string\",\n" +
                "  \"middleName\": \"string\",\n" +
                "  \"email\": \"hellotest@mail.ru\",\n" +
                "  \"birthdate\": \"2000-07-27\",\n" +
                "  \"passportSeries\": \"6082\",\n" +
                "  \"passportNumber\": \"170901\"\n" +
                "}";
        mockMvc.perform(post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void cantPrescoreYoung() throws Exception {
        String json = "{\n" +
                "  \"amount\": 10000,\n" +
                "  \"term\": 6,\n" +
                "  \"firstName\": \"string\",\n" +
                "  \"lastName\": \"string\",\n" +
                "  \"middleName\": \"string\",\n" +
                "  \"email\": \"hellotest@gmail.com\",\n" +
                "  \"birthdate\": \"20023-07-26\",\n" +
                "  \"passportSeries\": \"6565\",\n" +
                "  \"passportNumber\": \"173892\"\n" +
                "}";
        mockMvc.perform(post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void score() throws Exception {
        String json = "{\n" +
                "  \"amount\": 10000,\n" +
                "  \"term\": 6,\n" +
                "  \"firstName\": \"string\",\n" +
                "  \"lastName\": \"string\",\n" +
                "  \"middleName\": \"string\",\n" +
                "  \"gender\": \"Male\",\n" +
                "  \"birthdate\": \"2000-07-26\",\n" +
                "  \"passportSeries\": \"0002\",\n" +
                "  \"passportNumber\": \"505568\",\n" +
                "  \"passportIssueDate\": \"2023-07-26\",\n" +
                "  \"passportIssueBranch\": \"string\",\n" +
                "  \"maritalStatus\": \"Married\",\n" +
                "  \"dependentAmount\": 0,\n" +
                "  \"employment\": {\n" +
                "    \"employmentStatus\": \"Business owner\",\n" +
                "    \"employerINN\": \"string\",\n" +
                "    \"salary\": 0,\n" +
                "    \"position\": \"Default\",\n" +
                "    \"workExperienceTotal\": 0,\n" +
                "    \"workExperienceCurrent\": 0\n" +
                "  },\n" +
                "  \"account\": \"string\",\n" +
                "  \"isInsuranceEnabled\": true,\n" +
                "  \"isSalaryClient\": true\n" +
                "}";
        mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void cantScoreUnemployed() throws Exception {
        String json = "{\n" +
                "  \"amount\": 10000,\n" +
                "  \"term\": 6,\n" +
                "  \"firstName\": \"string\",\n" +
                "  \"lastName\": \"string\",\n" +
                "  \"middleName\": \"string\",\n" +
                "  \"gender\": \"Male\",\n" +
                "  \"birthdate\": \"2000-07-27\",\n" +
                "  \"passportSeries\": \"2348\",\n" +
                "  \"passportNumber\": \"027218\",\n" +
                "  \"passportIssueDate\": \"2023-07-27\",\n" +
                "  \"passportIssueBranch\": \"string\",\n" +
                "  \"maritalStatus\": \"Married\",\n" +
                "  \"dependentAmount\": 0,\n" +
                "  \"employment\": {\n" +
                "    \"employmentStatus\": \"Unemployed\",\n" +
                "    \"employerINN\": \"string\",\n" +
                "    \"salary\": 0,\n" +
                "    \"position\": \"Default\",\n" +
                "    \"workExperienceTotal\": 0,\n" +
                "    \"workExperienceCurrent\": 0\n" +
                "  },\n" +
                "  \"account\": \"string\",\n" +
                "  \"isInsuranceEnabled\": true,\n" +
                "  \"isSalaryClient\": true\n" +
                "}";
        mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andDo(print())
                .andExpect(status().isOk());
    }

}
