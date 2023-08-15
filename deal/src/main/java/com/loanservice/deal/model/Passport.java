package com.loanservice.deal.model;

import lombok.*;

import java.time.LocalDate;

@Data
public class Passport {

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
