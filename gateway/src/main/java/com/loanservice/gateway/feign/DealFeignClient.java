package com.loanservice.gateway.feign;

import com.loanservice.gateway.openapi.dto.FinishRegistrationRequestDTO;
import com.loanservice.gateway.openapi.dto.SesCodeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Feign client for conveyor service.
 */
@FeignClient(name = "deal", url = "${deal.url}")
public interface DealFeignClient {

    @RequestMapping(
            method = {RequestMethod.PUT},
            value = {"/calculate/{applicationId}"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<Void> finishRegistration(@PathVariable Long applicationId,
                                            @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO);


    @RequestMapping(
            method = {RequestMethod.PUT},
            value = {"/document/{applicationId}/send"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<Void> sendDocuments(@PathVariable Long applicationId);

    @RequestMapping(
            method = {RequestMethod.PUT},
            value = {"/document/{applicationId}/sign"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<Void> signDocuments(@PathVariable Long applicationId);

    @RequestMapping(
            method = {RequestMethod.PUT},
            value = {"/document/{applicationId}/code"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<Void> verifySES(@PathVariable Long applicationId,
                                   @RequestBody SesCodeRequest sesCodeRequest);



}
