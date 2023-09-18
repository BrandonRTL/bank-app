package com.loanservice.gateway.feign;

import com.loanservice.gateway.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.gateway.openapi.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "application", url = "${application.url}")
public interface ApplicationFeignClient {

    @RequestMapping(
            method = {RequestMethod.POST},
            value = {""},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<List<LoanOfferDTO>> prescore(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @RequestMapping(
            method = {RequestMethod.PUT},
            value = {"/offer"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<Void> applyOffer(LoanOfferDTO loanOfferDTO);
}
