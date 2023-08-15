package com.loanservice.deal.model;

import com.loanservice.deal.openapi.dto.ChangeType;
import feign.FeignException;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationStatusHistory {

    private String status;

    private LocalDateTime timestamp;

    private ChangeType type;

}
