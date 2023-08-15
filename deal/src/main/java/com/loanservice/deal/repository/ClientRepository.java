package com.loanservice.deal.repository;

import com.loanservice.deal.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
