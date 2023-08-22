package com.loanservice.application.service.impl;

import com.loanservice.application.feign.DealFeignClient;
import com.loanservice.application.openapi.dto.LoanApplicationRequestDTO;
import com.loanservice.application.openapi.dto.LoanOfferDTO;
import com.loanservice.application.service.ApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final DealFeignClient dealFeignClient;

    @Override
    public List<LoanOfferDTO> prescore(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("Start prescoring. Input: {}", loanApplicationRequestDTO);
        var response = dealFeignClient.prescore(loanApplicationRequestDTO);
        log.debug("Deal service response: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public void saveOffer(LoanOfferDTO dto) {
        log.debug("Start saving. Offer: {}", dto);
        dealFeignClient.chooseOffer(dto);
        log.debug("Offer saved");
    }
}
