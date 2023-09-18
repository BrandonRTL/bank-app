package com.loanservice.deal.mapper;

import com.loanservice.deal.model.Application;
import com.loanservice.deal.model.Credit;
import com.loanservice.deal.openapi.dto.ApplicationDTO;
import com.loanservice.deal.openapi.dto.CreditDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationDTO fromApplication(Application application);

    List<ApplicationDTO> fromList(List<Application> applications);

}
