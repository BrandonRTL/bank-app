package com.loanservice.application.feign;

import com.loanservice.application.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.application.openapi.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "deal", url = "${deal.url}")
public interface DealFeignClient {

    @RequestMapping(
            method = {RequestMethod.POST},
            value = {"/application"},
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
    ResponseEntity<Void> chooseOffer(LoanOfferDTO loanOfferDTO);
}
