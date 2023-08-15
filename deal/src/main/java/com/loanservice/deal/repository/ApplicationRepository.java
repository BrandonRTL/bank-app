package com.loanservice.deal.repository;

import com.loanservice.deal.model.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
