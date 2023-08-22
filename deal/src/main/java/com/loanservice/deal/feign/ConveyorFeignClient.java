package com.loanservice.deal.feign;

import com.loanservice.deal.openapi.dto.CreditDTO;
import com.loanservice.deal.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.deal.openapi.dto.LoanOfferDTO;
import com.loanservice.deal.openapi.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Feign client for conveyor service.
 */
@FeignClient(name = "conveyor", url = "${conveyor.url}")
public interface ConveyorFeignClient {

    @RequestMapping(
            method = {RequestMethod.POST},
            value = {"/offers"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<List<LoanOfferDTO>> prescore(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @RequestMapping(
            method = {RequestMethod.POST},
            value = {"/calculation"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<CreditDTO> score(@RequestBody ScoringDataDTO scoringDataDTO);
}
