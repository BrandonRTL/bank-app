package com.loanservice.dossier.feign;

import com.loanservice.dossier.openapi.dto.ApplicationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Feign client for conveyor service.
 */
@FeignClient(name = "deal", url = "${deal.url}")
public interface DealFeignClient {

    @RequestMapping(
            method = {RequestMethod.GET},
            value = {"/admin/application/{applicationId}"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<ApplicationDTO> getApplicationDTO(@PathVariable Long applicationId);

    @RequestMapping(
            method = {RequestMethod.PUT},
            value = {"/admin/application/{applicationId}/status"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<Void> updateApplicationStatus(@PathVariable Long applicationId);
}
