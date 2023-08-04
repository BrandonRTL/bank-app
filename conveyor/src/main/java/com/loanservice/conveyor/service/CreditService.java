package com.loanservice.conveyor.service;


import com.loanservice.conveyor.openapi.dto.CreditDTO;
import com.loanservice.conveyor.openapi.dto.ScoringDataDTO;

public interface CreditService {

    CreditDTO score(ScoringDataDTO scoringDataDTO);
}
