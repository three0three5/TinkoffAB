package org.example.accounts.domain;

import org.example.accounts.domain.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<AccountEntity, Integer> {

}
