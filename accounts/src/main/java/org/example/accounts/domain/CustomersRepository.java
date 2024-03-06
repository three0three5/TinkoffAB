package org.example.accounts.domain;

import org.example.accounts.domain.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomersRepository extends JpaRepository<CustomerEntity, Integer> {

}
